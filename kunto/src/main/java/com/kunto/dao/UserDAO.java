package com.kunto.dao;
//

//import java.sql.*;
//import java.util.*;
//import org.json.*;
//
//import com.kunto.util.Database;
//
//public class UserDAO {
//    private static UserDAO instance;
//    private Connection conn;
//
//    private UserDAO() {
//        conn = Database.getInstance().getConnection();
//    }
//
//    public static UserDAO getInstance() {
//        if (instance == null) {
//            synchronized (UserDAO.class) {
//                if (instance == null) {
//                    instance = new UserDAO();
//                }
//            }
//        }
//        return instance;
//    }
//
//    public JSONArray getUserFriends(int userId) throws SQLException {
//        String query = "SELECT u.id AS friend_id, u.username, u.about "
//                     + "FROM user_friends uf "
//                     + "JOIN users u ON u.id = (CASE WHEN uf.user_id = ? THEN uf.friend_id ELSE uf.user_id END) "
//                     + "WHERE uf.user_id = ? OR uf.friend_id = ?";
//
//        JSONArray userListArray = new JSONArray();
//        try (PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setInt(1, userId);
//            stmt.setInt(2, userId);
//            stmt.setInt(3, userId);
//
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                JSONObject userObject = new JSONObject();
//                userObject.put("id", "u" + rs.getInt("friend_id"));
//                userObject.put("name", rs.getString("username"));
//                userObject.put("about", rs.getString("about"));
//                userObject.put("type", "user");
//                userListArray.put(userObject);
//            }
//        }
//        return userListArray;
//    }
//}

import java.io.IOException;
import java.sql.*;
import java.util.*;
import org.json.*;

import com.kunto.config.DBConnection;
import com.kunto.util.PasswordUtil;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

public class UserDAO {
	private static UserDAO instance;

	private UserDAO() {

	}

	public static synchronized UserDAO getInstance() {
		if (instance == null) {
			instance = new UserDAO();
		}
		return instance;
	}

	public JSONArray getUserFriends(int userId, Map<String, Session> userSessionMap) {
		String query = "SELECT u.user_id AS friend_id, u.username, u.about " + "FROM user_friends uf "
				+ "JOIN users u ON u.user_id = (CASE WHEN uf.user_id = ? THEN uf.friend_id ELSE uf.user_id END) "
				+ "WHERE uf.user_id = ? OR uf.friend_id = ?";

		JSONArray userListArray = new JSONArray();
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, userId);
			stmt.setInt(2, userId);
			stmt.setInt(3, userId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				JSONObject userObject = new JSONObject();
				String username = rs.getString("username");
				String id = "u" + rs.getInt("friend_id");

				userObject.put("id", id);
				userObject.put("name", username);
				userObject.put("about", rs.getString("about"));
				userObject.put("type", "user");

				if (userSessionMap.containsKey(username)) {
					userObject.put("online", true);
				} else {
					userObject.put("online", false);
				}

				userListArray.put(userObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userListArray;
	}

//    public JSONArray getUserFriends(int userId) throws SQLException {
//      String query = "SELECT u.id AS friend_id, u.username, u.about "
//                   + "FROM user_friends uf "
//                   + "JOIN users u ON u.id = (CASE WHEN uf.user_id = ? THEN uf.friend_id ELSE uf.user_id END) "
//                   + "WHERE uf.user_id = ? OR uf.friend_id = ?";
//
//      JSONArray userListArray = new JSONArray();
//      try (PreparedStatement stmt = conn.prepareStatement(query)) {
//          stmt.setInt(1, userId);
//          stmt.setInt(2, userId);
//          stmt.setInt(3, userId);
//
//          ResultSet rs = stmt.executeQuery();
//
//          while (rs.next()) {
//              JSONObject userObject = new JSONObject();
//              userObject.put("id", "u" + rs.getInt("friend_id"));
//              userObject.put("name", rs.getString("username"));
//              userObject.put("about", rs.getString("about"));
//              userObject.put("type", "user");
//              userListArray.put(userObject);
//          }
//      }
//      return userListArray;
//  }
	public boolean updateUserStatusDB(int userId, boolean isOnline) {
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement stmt = conn.prepareStatement("UPDATE users SET status = ? WHERE user_id = ?");
			stmt.setString(1, isOnline ? "online" : "offline");
			stmt.setInt(2, userId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean notifyUserStatus(String userId, boolean isOnline, Map<String, Session> userSessionMap) {
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement stmt = conn
					.prepareStatement("UPDATE users SET status = ?::user_status WHERE user_id = ?");
			stmt.setString(1, isOnline ? "online" : "offline");
			stmt.setInt(2, Integer.parseInt(userId.substring(1)));
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error updating user status: " + e.getMessage());
			e.printStackTrace();
		}

		// Fetch user friends from DB
		// String query = "SELECT friend_id FROM user_friends WHERE user_id = ? UNION
		// SELECT user_id FROM user_friends WHERE friend_id = ?";
		String query = "SELECT u.user_id AS friend_id, u.username, u.about " + "FROM user_friends uf "
				+ "JOIN users u ON u.user_id = (CASE WHEN uf.user_id = ? THEN uf.friend_id ELSE uf.user_id END) "
				+ "WHERE uf.user_id = ? OR uf.friend_id = ?";
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			int id = Integer.parseInt(userId.substring(1));
			stmt.setInt(1, id);
			stmt.setInt(2, id);
			stmt.setInt(3, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String friendId = rs.getString("username");
				if (userSessionMap.containsKey(friendId)) {
					Session friendSession = userSessionMap.get(friendId);
					JSONObject updateOnline = new JSONObject();
					updateOnline.put("type", "updateOnline");
					updateOnline.put("id", userId);
					updateOnline.put("online", isOnline);
					friendSession.getBasicRemote().sendText(updateOnline.toString());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		return true;
	}

	public int getUserIdFromUsername(String username) {
		int userId = -1; // Default to -1 if not found
		Connection conn = null;

		try {
			conn = DBConnection.getInstance().getConnection();
			String query = "SELECT user_id FROM users WHERE username = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				userId = rs.getInt("user_id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userId;
	}
	/*
	 * public boolean addFriend(int userId,int friendId) { Connection conn = null;
	 * try { conn = DBConnection.getInstance().getConnection();
	 * 
	 * 
	 * String query = "INSERT INTO user_friends (user_id, friend_id) VALUES (?, ?)";
	 * 
	 * 
	 * 
	 * PreparedStatement stmt = conn.prepareStatement(query);
	 * 
	 * // Set the parameters: userId and friendId stmt.setInt(1, userId);
	 * stmt.setInt(2, friendId);
	 * 
	 * // Execute the update to insert the friendship int rowsAffected =
	 * stmt.executeUpdate(); return rowsAffected>0; // Send a success message if the
	 * friendship was added
	 * 
	 * response.setContentType("application/json"); response.getWriter().
	 * write("{\"status\":\"success\",\"message\":\"Friend added successfully\"}");
	 * } else { response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	 * 
	 * }
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); return false;
	 * response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	 * response.getWriter().
	 * write("{\"status\":\"error\",\"message\":\"Database error\"}"); }
	 * 
	 * }
	 */
	  public boolean addFriend(int userId, int friendId) {
	        Connection conn = null;
	        try {
	            conn = DBConnection.getInstance().getConnection();

	            // Ensure user_id is always smaller than friend_id
	            int smallerId = Math.min(userId, friendId);
	            int largerId = Math.max(userId, friendId);

	            String query = "INSERT INTO user_friends (user_id, friend_id) VALUES (?, ?)";

	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setInt(1, smallerId);
	            stmt.setInt(2, largerId);

	            int rowsAffected = stmt.executeUpdate();
	            return rowsAffected > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	public void addUserToCommonGroups(int userId) {
		String insertGroupsSQL = "INSERT INTO user_groups (user_id, group_id) "
				+ "SELECT ?, id FROM groups WHERE id IN (1, 2, 3)";

		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement stmt = conn.prepareStatement(insertGroupsSQL);
			stmt.setInt(1, userId);
			int rowsInserted = stmt.executeUpdate();

			if (rowsInserted > 0) {
				System.out.println("User " + userId + " added to common groups successfully.");
			} else {
				System.out.println("No groups found to add user " + userId + ".");
			}

		} catch (SQLException e) {
			System.err.println("Error adding user to groups: " + e.getMessage());

		}
	}

	public int insertUser(String email, String username, String password) {
		String insertQuery = "INSERT INTO users (email, password_hash, username) VALUES (?, ?, ?) RETURNING user_id";
		int userId = -1;

		try {
			Connection connection = DBConnection.getInstance().getConnection();
			PreparedStatement stmt = connection.prepareStatement(insertQuery);
			stmt.setString(1, email);
			stmt.setString(2, PasswordUtil.hashPassword(password)); // Hash the password
			stmt.setString(3, username);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				userId = rs.getInt("user_id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userId;
	}

	public void insertUserStarHistory(int userId) {
		String insertStarQuery = "INSERT INTO user_star_history (user_id, last_active_date, stars) VALUES (?, CURRENT_DATE, 6)";

		try {
			Connection connection = DBConnection.getInstance().getConnection();
			PreparedStatement stmt = connection.prepareStatement(insertStarQuery);
			stmt.setInt(1, userId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
