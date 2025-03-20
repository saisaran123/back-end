package com.kunto.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.config.*;

public class getPersonalInfo{
	public static JSONObject getUserBasicInfo(int user_id) {
	    JSONObject userInfo = new JSONObject();
	    String query = "SELECT user_id, username, email, status, about FROM users WHERE user_id = ?";

	    try {
	    	Connection conn = DBConnection.getInstance().getConnection();
	    
	         PreparedStatement pstmt = conn.prepareStatement(query);

	        pstmt.setInt(1, user_id);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            userInfo.put("user_id", rs.getInt("user_id"));
	            userInfo.put("username", rs.getString("username"));
	            userInfo.put("email", rs.getString("email"));
	            userInfo.put("status", rs.getString("status"));
	            userInfo.put("about", rs.getString("about"));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return userInfo;
	}
	
public static JSONObject getPersonalInfo(int user_id) {
        JSONObject userInfo = new JSONObject();
        String query = "SELECT user_id, user_name, gender, country, city, level, height, weight, " +
                       "active_level, date_of_birth, goal, user_profile_picture " +
                       "FROM user_details WHERE user_id = ?"; // Changed 'users' to 'user_details'

        try {Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, user_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userInfo.put("user_id", rs.getInt("user_id"));
                    userInfo.put("username", rs.getString("user_name")); // Keep "user_name" consistent
                    userInfo.put("gender", rs.getString("gender"));
                    userInfo.put("country", rs.getString("country"));
                    userInfo.put("city", rs.getString("city"));
                    userInfo.put("level", rs.getString("level"));
                    userInfo.put("height", rs.getInt("height"));
                    userInfo.put("weight", rs.getInt("weight"));
                    userInfo.put("active_level", rs.getString("active_level"));
                    userInfo.put("date_of_birth", rs.getDate("date_of_birth").toString());
                    userInfo.put("goal", rs.getString("goal"));
                    userInfo.put("user_profile_picture", rs.getString("user_profile_picture"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userInfo;
    }
	public  static JSONArray getCalorieBurnedData(int userId) {
        JSONArray calorieData = new JSONArray();
        String query = "SELECT entry_id, user_id, calorie_burned, date_logged, activity_name, workout_time FROM calorie_burned where user_id=?";

        try {Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             pstmt.setInt(1, userId);
             ResultSet rs = pstmt.executeQuery(); 

            while (rs.next()) {
                JSONObject entry = new JSONObject();
                entry.put("entry_id", rs.getInt("entry_id"));
                entry.put("user_id", rs.getInt("user_id"));
                entry.put("calorie_burned", rs.getDouble("calorie_burned"));
                entry.put("date_logged", rs.getDate("date_logged").toString());
                entry.put("activity_name", rs.getString("activity_name"));
                entry.put("workout_time", rs.getTime("workout_time").toString());
                calorieData.put(entry);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Replace with proper logging in production
        }

        return calorieData;
    }
	
	public static JSONArray getWalkLogData(int user_id) {
        JSONArray walkLogData = new JSONArray();
        String query = "SELECT entry_id, user_id, date_logged, km_walked, calorie_burned FROM walk_log where user_id=?";

        try {Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             pstmt.setInt(1, user_id);
             ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                JSONObject entry = new JSONObject();
                entry.put("entry_id", rs.getInt("entry_id"));
                entry.put("user_id", rs.getInt("user_id"));
                entry.put("date_logged", rs.getDate("date_logged").toString());
                entry.put("km_walked", rs.getDouble("km_walked"));
                entry.put("calorie_burned", rs.getDouble("calorie_burned"));
                walkLogData.put(entry);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Replace with proper logging in production
        }

        return walkLogData;
    }
	
	public static JSONArray getDailyNutritionData(int user_id) {
        JSONArray nutritionData = new JSONArray();
        String query = "SELECT id, user_id, log_date, calories, protein, fiber, carbs, fat FROM daily_nutrition WHERE user_id = ?";

        try {Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query) ;

            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                JSONObject entry = new JSONObject();
                entry.put("id", rs.getInt("id"));
                entry.put("user_id", rs.getInt("user_id"));
                entry.put("log_date", rs.getDate("log_date").toString());
                entry.put("calories", rs.getDouble("calories"));
                entry.put("protein", rs.getDouble("protein"));
                entry.put("fiber", rs.getDouble("fiber"));
                entry.put("carbs", rs.getDouble("carbs"));
                entry.put("fat", rs.getDouble("fat"));
                
                nutritionData.put(entry);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Use proper logging in production
        }

        return nutritionData;
    }
	
    public static JSONArray getAllFriends(int user_id) {
        JSONArray friendsList = new JSONArray();
        String query = "SELECT u.username, u.status " +
                       "FROM user_friends uf " +
                       "JOIN users u ON uf.friend_id = u.user_id " +
                       "WHERE uf.user_id = ?";

        try {Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                JSONObject friend = new JSONObject();
                friend.put("username", rs.getString("username"));
                friend.put("status", rs.getString("status"));
                
                friendsList.put(friend);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Use proper logging in production
        }

        return friendsList;
    }
}