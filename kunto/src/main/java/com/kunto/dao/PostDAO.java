package com.kunto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.models.Comment;
import com.kunto.models.LeaderboardEntry;
import com.kunto.models.Like;
import com.kunto.models.Post;
import com.kunto.models.TrendingUser;
import com.kunto.models.UserLikes;

public class PostDAO {
	
	
	private static PostDAO instance;

    private PostDAO() {}

    public static synchronized PostDAO getInstance() {
        if (instance == null) {
            instance = new PostDAO();
        }
        return instance;
    }
	
	public boolean toggleLike(Like like) {
        try {
        	Connection conn = DBConnection.getInstance().getConnection();
            String checkSql = "SELECT * FROM likes WHERE user_id = ? AND post_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, like.getUserId());
            checkStmt.setInt(2, like.getPostId());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String deleteSql = "DELETE FROM likes WHERE user_id = ? AND post_id = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setInt(1, like.getUserId());
                deleteStmt.setInt(2, like.getPostId());
                deleteStmt.executeUpdate();
                return false;
            } else {
                String insertSql = "INSERT INTO likes (user_id, post_id) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, like.getUserId());
                insertStmt.setInt(2, like.getPostId());
                insertStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	 public List<Post> getPosts(int from, int to) {
	        List<Post> posts = new ArrayList<>();
	        System.out.println(from +" from ;to  "+to);
	        String totalQuery = "SELECT MAX(id) AS max FROM posts";
	        String query = "SELECT p.id,p.views, p.user_id, p.content, p.title, p.image_url, p.created_at, u.username " +
	                       "FROM posts p INNER JOIN users u ON p.user_id = u.user_id " +
	                       "ORDER BY p.created_at DESC LIMIT ? OFFSET ?";

	        try {
	        	Connection conn = DBConnection.getInstance().getConnection();
	             PreparedStatement totalStmt = conn.prepareStatement(totalQuery);
	             ResultSet totalRs = totalStmt.executeQuery();

	            totalRs.next();
	            int totalPosts = totalRs.getInt("max");
	            
	            if(to  > totalPosts){
	            	return posts;
	            }

	            PreparedStatement stmt = conn.prepareStatement(query);
	                stmt.setInt(1, (to - from + totalPosts) % totalPosts);
	                stmt.setInt(2, from % totalPosts);

	                try (ResultSet rs = stmt.executeQuery()) {
	                    while (rs.next()) {
	                        Post post = new Post(
	                            rs.getInt("id"),
	                            rs.getInt("user_id"),
	                            rs.getString("username"),
	                            rs.getString("content"),
	                            rs.getString("title"),
	                            rs.getString("image_url"),
	                            rs.getTimestamp("created_at").toString(),
	                            rs.getInt("views")
	                        );
	                        posts.add(post);
	                    }
	                }
	            
	        }catch (Exception e) {
				e.printStackTrace();
			}
	        
	        return posts;
	    }
	 
	 
	 public JSONObject getLikeDetails(int postId, int userId)  {
		 
		 	System.out.println(postId+" "+userId);
	        String query = """
	            SELECT COUNT(*) AS total_likes,
	                   EXISTS (
	                       SELECT 1 FROM likes l2 
	                       WHERE l2.post_id = ? AND l2.user_id = ?
	                   ) AS user_liked,
	                   json_agg(u.username) AS liked_users
	            FROM likes l
	            JOIN users u ON l.user_id = u.user_id
	            WHERE l.post_id = ?;
	        """;

	        try {
	        	Connection conn = DBConnection.getInstance().getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setInt(1, postId);
	            pstmt.setInt(2, userId);
	            pstmt.setInt(3, postId);

	            ResultSet rs = pstmt.executeQuery();
	            JSONObject result = new JSONObject();

	            if (rs.next()) {
	                if (rs.getString("liked_users") == null) {
	                    result.put("likedUsers", false);
	                } else {
	                    result.put("likedUsers", new JSONArray(rs.getString("liked_users")));
	                }
	                result.put("totalLikes", rs.getInt("total_likes"));
	                result.put("userLiked", rs.getBoolean("user_liked"));
	            }

	            return result;
	        }catch(SQLException e) {
	        	e.printStackTrace();
	        	return null;
	        }
	    }
	 
	 
	 public boolean addComment(Comment comment){
	        String query = "INSERT INTO comments (post_id, user_id, comment) VALUES (?, ?, ?)";

	        try {
	        	Connection conn = DBConnection.getInstance().getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setInt(1, comment.getPostId());
	            pstmt.setInt(2, comment.getUserId());
	            pstmt.setString(3, comment.getComment());

	            int rowsAffected = pstmt.executeUpdate();
	            return rowsAffected > 0;
	        }
	        catch(SQLException e) {
	        	e.printStackTrace();
	        	return false;
	        }
	    }
	 
	 public int createPost(Post post) throws SQLException {
	        String sql = "INSERT INTO posts (title, content, image_url, user_id) VALUES (?, ?, ?, ?)";

	        try {
	        	Connection conn = DBConnection.getInstance().getConnection();
	            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	            stmt.setString(1, post.getTitle());
	            stmt.setString(2, post.getContent());
	            stmt.setString(3, post.getImageUrl());
	            stmt.setInt(4, post.getUserId());

	            int rowsInserted = stmt.executeUpdate();
	            if (rowsInserted > 0) {
	                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        return generatedKeys.getInt(1);
	                    }
	                }
	            }
	            return -1;
	        }
	        catch(SQLException e) {
	        	e.printStackTrace();
	        	return -1;
	        }
	    }
	 

	    public List<Comment> getComments(int postId)  {
	       // String query = "SELECT user_id, comment FROM comments WHERE post_id = ? ORDER BY id ASC";
	        String query = "SELECT c.* ,u.username FROM comments c join users u on c.user_id=u.user_id WHERE post_id = ? ORDER BY  c.created_at DESC";
	        List<Comment> comments = new ArrayList<>();

	        try {
	        	Connection conn = DBConnection.getInstance().getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(query);

	            pstmt.setInt(1, postId);
	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                int userId = rs.getInt("user_id");
	                String commentText = rs.getString("comment");
	                String username = rs.getString("username");
	                Timestamp createAt = rs.getTimestamp("created_at");
	                comments.add(new Comment(postId, userId, commentText,username,createAt));
	            }
	        }
	        catch(SQLException e) {
	        	e.printStackTrace();
	        	
	        }
	        return comments;
	    }
	    
	    
	    
	    public boolean hasUserViewedPost(int postId, int userId) {
	        String sql = "SELECT 1 FROM post_views WHERE post_id = ? AND user_id = ?";
	        try  {
	        	Connection conn = DBConnection.getInstance().getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setInt(1, postId);
	            stmt.setInt(2, userId);
	            ResultSet rs = stmt.executeQuery();
	            return rs.next();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

	    public void incrementViewCount(int postId, int userId) {
	        String insertViewSql = "INSERT INTO post_views (post_id, user_id) VALUES (?, ?)";
	        String updatePostSql = "UPDATE posts SET views = views + 1 WHERE id = ?";

	        try {
	        	Connection conn =DBConnection.getInstance().getConnection();
	            conn.setAutoCommit(false);

	            try  {
	            	PreparedStatement insertStmt = conn.prepareStatement(insertViewSql);
	                 PreparedStatement updateStmt = conn.prepareStatement(updatePostSql);
	                insertStmt.setInt(1, postId);
	                insertStmt.setInt(2, userId);
	                insertStmt.executeUpdate();

	                updateStmt.setInt(1, postId);
	                updateStmt.executeUpdate();

	                conn.commit();
	            } catch (SQLException e) {
	                conn.rollback();
	                e.printStackTrace();
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public int getPostViewCount(int postId) {
	        String sql = "SELECT views FROM posts WHERE id = ?";
	        try {
	        	Connection conn = DBConnection.getInstance().getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setInt(1, postId);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return rs.getInt("views");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }

	    
	    public List<LeaderboardEntry> getTopUsersByPostCount(int limit) {
	        List<LeaderboardEntry> leaderboard = new ArrayList<>();
	        //String sql = "SELECT user_id, COUNT(*) AS post_count FROM posts GROUP BY user_id ORDER BY post_count DESC LIMIT ?";
	        String sql="SELECT \n"
	        		+ "    u.username, \n"
	        		+ "    p.user_id, \n"
	        		+ "    COUNT(*) AS post_count \n"
	        		+ "FROM \n"
	        		+ "    posts p\n"
	        		+ "JOIN \n"
	        		+ "    users u ON p.user_id = u.user_id\n"
	        		+ "GROUP BY \n"
	        		+ "    p.user_id, u.username\n"
	        		+ "ORDER BY \n"
	        		+ "    post_count DESC\n"
	        		+ "LIMIT ?";
	  

	        try {
	        	Connection conn = DBConnection.getInstance().getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, limit);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                while (rs.next()) {
	                    int userId = rs.getInt("user_id");
	                    String  username = rs.getString("username");
	                    int postCount = rs.getInt("post_count");
	                    leaderboard.add(new LeaderboardEntry(username,userId, postCount));
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return leaderboard;
	    }

	    public List<UserLikes> getMostLikedPosters(int limit) {
	        String sql = "SELECT u.user_id, u.username, COUNT(l.id) AS total_likes " +
	                     "FROM posts p " +
	                     "JOIN users u ON p.user_id = u.user_id " +
	                     "JOIN likes l ON p.id = l.post_id " +
	                     "GROUP BY u.user_id, u.username " +
	                     "ORDER BY total_likes DESC " +
	                     "LIMIT ?";
	        List<UserLikes> list = new ArrayList<>();
	        try  {
	        	Connection conn = DBConnection.getInstance().getConnection();
	        	PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setInt(1, limit);
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                UserLikes userLikes = new UserLikes(
	                    rs.getInt("user_id"),
	                    rs.getString("username"),
	                    rs.getInt("total_likes")
	                );
	                list.add(userLikes);
	            }
	        }
	        catch(SQLException e) {
	        	e.printStackTrace();
	        	
	        }
	        return list;
	    }

	    // Get Trending Users based on likes + comments in last 7 days
	    public List<TrendingUser> getTrendingUsers(int limit)  {
	        String sql = "SELECT u.user_id, u.username, COUNT(l.id) + COUNT(c.id) AS engagement_score " +
	                     "FROM posts p " +
	                     "JOIN users u ON p.user_id = u.user_id " +
	                     "LEFT JOIN likes l ON p.id = l.post_id AND l.id IN ( " +
	                     "    SELECT id FROM likes WHERE CURRENT_TIMESTAMP - INTERVAL '7 days' <= (SELECT created_at FROM posts WHERE id = l.post_id) " +
	                     ") " +
	                     "LEFT JOIN comments c ON p.id = c.post_id AND c.created_at >= NOW() - INTERVAL '7 days' " +
	                     "GROUP BY u.user_id, u.username " +
	                     "ORDER BY engagement_score DESC " +
	                     "LIMIT ?";
	        List<TrendingUser> list = new ArrayList<>();
	        try  {
	        	Connection conn = DBConnection.getInstance().getConnection();
	        	PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setInt(1, limit);
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                TrendingUser trendingUser = new TrendingUser(
	                    rs.getInt("user_id"),
	                    rs.getString("username"),
	                    rs.getInt("engagement_score")
	                );
	                list.add(trendingUser);
	            }
	        }
	        catch(SQLException e) {
	        	e.printStackTrace();
	        	
	        }
	        return list;
	    }
}
