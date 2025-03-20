
package com.kunto.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.models.ContributorUser;
import com.kunto.models.FAQ;
import com.kunto.models.FAQAnswer;
import com.kunto.models.LeaderboardEntry;
import com.kunto.models.TrendingUser;

public class FAQDao {
	private static volatile FAQDao instance;

   // private Connection connection;

    private FAQDao() {
      
    }

    public static synchronized FAQDao getInstance() {
        if (instance == null) {
            instance = new FAQDao() ;
        }
        return instance;
    }

    public JSONObject getFaqLikeHistory(int answerId, int userId) {
        JSONObject result = new JSONObject();
        String query = "SELECT COUNT(*) AS total_likes, SUM(CASE WHEN user_id = ? THEN 1 ELSE 0 END) AS user_liked FROM faq_likes WHERE answer_id = ?";
        try {
        	Connection conn = DBConnection.getInstance().getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, answerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result.put("totalLikes", rs.getInt("total_likes"));
                result.put("userLiked", rs.getInt("user_liked") > 0);
            }
        }catch(Exception e) {
        	e.printStackTrace();
        	
        }
        return result;
    }

    public boolean toggleFaqLike(int answerId, int userId){
        if (userHasLiked(answerId, userId)) {
            deleteFaqLike(answerId, userId);
            return false;
        } else {
            insertFaqLike(answerId, userId);
            return true;
        }
    }

    private boolean userHasLiked(int answerId, int userId) {
        String query = "SELECT 1 FROM faq_likes WHERE answer_id = ? AND user_id = ?";
        try{
        	Connection conn = DBConnection.getInstance().getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, answerId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }

    private void deleteFaqLike(int answerId, int userId) {
        String query = "DELETE FROM faq_likes WHERE answer_id = ? AND user_id = ?";
        try {
        	Connection conn = DBConnection.getInstance().getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, answerId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void insertFaqLike(int answerId, int userId)  {
        String query = "INSERT INTO faq_likes (answer_id, user_id) VALUES (?, ?)";
        try  {
        	Connection conn = DBConnection.getInstance().getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, answerId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public boolean addFaqAnswer(int questionId, int userId, String answerText){
        String query = "INSERT INTO faq_answers (question_id, user_id, answer_text) VALUES (?, ?, ?)";
        try  {
        	Connection conn = DBConnection.getInstance().getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, questionId);
            stmt.setInt(2, userId);
            stmt.setString(3, answerText);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }catch(Exception e) {
        	e.printStackTrace();
        	return false;
        }
    }
    

    public List<FAQ> getFAQs(int from, int to) {
        List<FAQ> faqList = new ArrayList<>();
        String totalQuery = "SELECT MAX(faq_id) AS max FROM faq_questions";
        String query = "SELECT q.faq_id,q.views, q.user_id, q.title, q.question_text, q.created_at, u.username " +
                       "FROM faq_questions q INNER JOIN users u ON q.user_id = u.user_id " +
                       "ORDER BY q.created_at DESC LIMIT ? OFFSET ?";

        try  {
        	Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement tstmt = conn.prepareStatement(totalQuery);
            ResultSet r = tstmt.executeQuery();
            r.next();
            int totalFAQs = r.getInt("max");
            
            if(to  > totalFAQs){
            	return faqList;
            }
           
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, (to - from + totalFAQs) % totalFAQs);
            stmt.setInt(2, from % totalFAQs);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FAQ faq = new FAQ();
                faq.setId(rs.getInt("faq_id"));
                faq.setUserId(rs.getInt("user_id"));
                faq.setUsername(rs.getString("username"));
                faq.setTitle(rs.getString("title"));
                faq.setQuestionText(rs.getString("question_text"));
                faq.setCreatedAt(rs.getTimestamp("created_at"));
                faq.setViews(rs.getInt("views"));

                faqList.add(faq);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return faqList;
    }

    public List<FAQAnswer> getAnswersForFAQ(int faqId) {
        List<FAQAnswer> answers = new ArrayList<>();
        String query = "SELECT fa.*, u.username " +
                "FROM faq_answers fa " +
                "JOIN users u ON fa.user_id = u.user_id " +
                "WHERE fa.question_id = ? " +
                "ORDER BY fa.created_at DESC";
        
        try  {
        	
        	Connection conn =DBConnection.getInstance().getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, faqId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                FAQAnswer answer = new FAQAnswer();
                int answerId=rs.getInt("id");
                int userId=rs.getInt("user_id");
                answer.setAnswerId(answerId);
                answer.setUsername(rs.getString("username"));
                answer.setFaqId(rs.getInt("question_id"));
                answer.setUserId(userId);
                answer.setAnswer(rs.getString("answer_text"));
                answer.setCreatedAt(rs.getTimestamp("created_at"));
                boolean isUserLiked=userHasLikedAnswer(answerId,userId,conn);
                int likeCount= getFaqAnswerLikeCount(answerId,conn);
                answer.setUserLIked(isUserLiked);
                answer.setLikeCount(likeCount);
                
                answers.add(answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }
    
    
    public boolean toggleFaqAnswerLike(int answerId, int userId,Connection conn) {
        if (userHasLikedAnswer(answerId, userId,conn)) {
            deleteFaqAnswerLike(answerId, userId,conn);
            return false;
        } else {
            insertFaqAnswerLike(answerId, userId,conn);
            return true;
        }
    }

    private boolean userHasLikedAnswer(int answerId, int userId,Connection conn) {
    	System.out.println(answerId +" "+userId);
        String query = "SELECT 1 FROM faq_likes WHERE answer_id = ? AND user_id = ?";
        try {
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, answerId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
        catch(SQLException e){
        	System.out.println("error userHasLikedAnswer");
        	e.printStackTrace();
        	return false;
        	
        }
    }
    public boolean userHasLikedQuestion(int postId, int userId,Connection conn) {
    	System.out.println(postId +" "+userId);
        String query = "SELECT 1 FROM faq_question_likes WHERE question_id = ? AND user_id = ?";
        try {
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
        catch(SQLException e){
        	System.out.println("error userHasLikedQuestion");
        	e.printStackTrace();
        	return false;
        	
        }
    }

    private void deleteFaqAnswerLike(int answerId, int userId,Connection conn)  {
        String query = "DELETE FROM faq_likes WHERE answer_id = ? AND user_id = ?";
        try {
        	
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, answerId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }catch(SQLException e){
        	e.printStackTrace();
        	
        }
    }

    private void insertFaqAnswerLike(int answerId, int userId,Connection conn) {
        String query = "INSERT INTO faq_likes (answer_id, user_id) VALUES (?, ?)";
        try {
        	
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, answerId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
        catch(SQLException e){
        	e.printStackTrace();
        	
        	
        }
    }

    public int getFaqAnswerLikeCount(int answerId,Connection conn) {
        String query = "SELECT COUNT(*) FROM faq_likes WHERE answer_id = ?";
        try {
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, answerId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }catch(SQLException e){
        	e.printStackTrace();
        	return -1;
        	
        }
    }
    
    
    
 
    public boolean hasUserLikedQuestion(int userId, int questionId,Connection conn) {
        String query = "SELECT COUNT(*) FROM faq_question_likes WHERE user_id = ? AND question_id = ?";
        try {
       
        	PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        catch(SQLException e){
        	e.printStackTrace();
        	
        	
        }
        return false;
    }

       public boolean toggleQuestionLike(int userId, int questionId,Connection conn)  {
        if (hasUserLikedQuestion(userId, questionId,conn)) {
         
            String deleteQuery = "DELETE FROM faq_question_likes WHERE user_id = ? AND question_id = ?";
            try  {
            	
            	PreparedStatement stmt = conn.prepareStatement(deleteQuery);
                stmt.setInt(1, userId);
                stmt.setInt(2, questionId);
                stmt.executeUpdate();
            }
            catch(SQLException e){
            	e.printStackTrace();
            	
            	
            }
            return false;
        } else {
            
            String insertQuery = "INSERT INTO faq_question_likes (user_id, question_id) VALUES (?, ?)";
            try {
            
            	PreparedStatement stmt = conn.prepareStatement(insertQuery);
                stmt.setInt(1, userId);
                stmt.setInt(2, questionId);
                stmt.executeUpdate();
            }
            catch(SQLException e){
            	e.printStackTrace();
            	
            	
            }
            return true; 
        }
    }

   
    public int getQuestionLikeCount(int questionId,Connection conn){
        String query = "SELECT COUNT(*) FROM faq_question_likes WHERE question_id = ?";
        try {
        	PreparedStatement stmt = conn.prepareStatement( query );
            stmt.setInt(1, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            catch(SQLException e){
            	e.printStackTrace();
            	
            	
            }
        }
        catch(SQLException e){
        	e.printStackTrace();
        	;
        	
        }
        return 0;
    }

    
    public int createFAQ(int userId, String title, String questionText,Connection conn) {
        String sql = "INSERT INTO faq_questions (user_id, title, question_text, created_at) VALUES (?, ?, ?, NOW()) RETURNING faq_id";
        try {
        	PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, questionText);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("faq_id"); // Return generated FAQ ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failure
    }
    
    
    public boolean hasUserViewedFAQ(int faqId, int userId) {
        String sql = "SELECT 1 FROM faq_views WHERE faq_id = ? AND user_id = ?";
        try {
        	Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, faqId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void incrementFAQViewCount(int faqId, int userId) {
        String insertViewSql = "INSERT INTO faq_views (faq_id, user_id) VALUES (?, ?)";
        String updateFAQSql = "UPDATE faq_questions SET views = views + 1 WHERE faq_id = ?";

        try {
        	Connection conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            try {
            	PreparedStatement insertStmt = conn.prepareStatement(insertViewSql);
                PreparedStatement updateStmt = conn.prepareStatement(updateFAQSql);
                insertStmt.setInt(1, faqId);
                insertStmt.setInt(2, userId);
                insertStmt.executeUpdate();

                updateStmt.setInt(1, faqId);
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

    public int getFAQViewCount(int faqId) {
        String sql = "SELECT views FROM faq_questions WHERE faq_id = ?";
        try {
        	Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, faqId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("views");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public List<LeaderboardEntry> getTopUsersByFAQCount(int limit) {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        //String sql = "SELECT user_id, COUNT(*) AS post_count FROM posts GROUP BY user_id ORDER BY post_count DESC LIMIT ?";
        String sql="SELECT \n"
        		+ "    u.username, \n"
        		+ "    p.user_id, \n"
        		+ "    COUNT(*) AS faq_count \n"
        		+ "FROM \n"
        		+ "    faq_questions p\n"
        		+ "JOIN \n"
        		+ "    users u ON p.user_id = u.user_id\n"
        		+ "GROUP BY \n"
        		+ "    p.user_id, u.username\n"
        		+ "ORDER BY \n"
        		+ "    faq_count DESC\n"
        		+ "LIMIT ?";
  

        try {
        	Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String  username = rs.getString("username");
                    int postCount = rs.getInt("faq_count");
                    leaderboard.add(new LeaderboardEntry(username,userId, postCount));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return leaderboard;
    }
    
    public List<TrendingUser> getTrendingUsers(int limit) throws SQLException {
        List<TrendingUser> trendingUsers = new ArrayList<>();
        String query = "SELECT u.user_id, u.username, " +
                "(COUNT(DISTINCT fq.faq_id) * 2 + COUNT(DISTINCT fa.id) * 3 + " +
                "COUNT(DISTINCT fql.question_id) + COUNT(DISTINCT fl.answer_id) * 2 + COUNT(DISTINCT fv.id)) AS engagement_score " +
                "FROM users u " +
                "LEFT JOIN faq_questions fq ON u.user_id = fq.user_id " +
                "LEFT JOIN faq_answers fa ON u.user_id = fa.user_id " +
                "LEFT JOIN faq_question_likes fql ON fq.faq_id = fql.question_id " +
                "LEFT JOIN faq_likes fl ON fa.id = fl.answer_id " +
                "LEFT JOIN faq_views fv ON fq.faq_id = fv.faq_id " +
                "GROUP BY u.user_id " +
                "ORDER BY engagement_score DESC " +
                "LIMIT ?";

        try {
        	Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                int score = rs.getInt("engagement_score");
                trendingUsers.add(new TrendingUser(userId, username, score));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return trendingUsers;
    }
    
    
    
    public List<ContributorUser> getTopContributors(int limit) throws SQLException {
        List<ContributorUser> contributors = new ArrayList<>();
        String query = "SELECT u.user_id, u.username, " +
                "(COUNT(DISTINCT fa.id) + COUNT(DISTINCT fl.answer_id) + (COUNT(DISTINCT fql.question_id) / 2)) AS contributor_score " +
                "FROM users u " +
                "LEFT JOIN faq_answers fa ON u.user_id = fa.user_id " +
                "LEFT JOIN faq_likes fl ON fa.id = fl.answer_id " +
                "LEFT JOIN faq_question_likes fql ON u.user_id = fql.user_id " +
                "GROUP BY u.user_id " +
                "ORDER BY contributor_score DESC " +
                "LIMIT ?";

        try  {
        	Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contributors.add(new ContributorUser(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getInt("contributor_score")
                ));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return contributors;
    }
    
    public List<ContributorUser> getMostLikedUsers(int limit) throws SQLException {
        List<ContributorUser> likedUsers = new ArrayList<>();
        String query = "SELECT u.user_id, u.username, " +
                "(COUNT(DISTINCT fql.question_id) + COUNT(DISTINCT fl.answer_id)) AS like_score " +
                "FROM users u " +
                "LEFT JOIN faq_question_likes fql ON u.user_id = fql.user_id " +
                "LEFT JOIN faq_likes fl ON u.user_id = fl.user_id " +
                "GROUP BY u.user_id " +
                "ORDER BY like_score DESC " +
                "LIMIT ?";

        try  {
        	Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                likedUsers.add(new ContributorUser(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getInt("like_score")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return likedUsers;
    }




}