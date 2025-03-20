package com.kunto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.config.DBConnection;

@WebServlet("/updateUserProfile")
public class UpdateUserProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("i'm here in update");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.getWriter().write("{\"error\": \"User not authenticated\"}");
            return;
        }
        
        int userId = (int) session.getAttribute("user_id");
        
        // Read JSON request body
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        JSONObject requestData = new JSONObject(sb.toString());
        
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        try {
        	System.out.println("i'm here in update");
            conn = DBConnection.getInstance().getConnection();
            
            // Update user_details table
            String updateUserDetails = "UPDATE user_details SET user_name = ?, height = ?, weight = ?, user_profile_picture = ?, active_level = ? WHERE user_id = ?";
            stmt1 = conn.prepareStatement(updateUserDetails);
            stmt1.setString(1, requestData.optString("user_name"));
            stmt1.setInt(2, requestData.optInt("height"));
            stmt1.setInt(3, requestData.optInt("weight"));
            stmt1.setString(4, requestData.optString("user_profile_picture"));
            stmt1.setString(5, requestData.optString("activity_level"));
            stmt1.setInt(6, userId);
            stmt1.executeUpdate();
            System.out.println("i'm here in update 1");
            // Update users table
            String updateUsers = "UPDATE users SET about = ? WHERE user_id = ?";
            stmt2 = conn.prepareStatement(updateUsers);
            stmt2.setString(1, requestData.optString("about"));
            stmt2.setInt(2, userId);
            stmt2.executeUpdate();
            System.out.println("i'm here in update 2");

            response.getWriter().write("{\"message\": \"Profile updated successfully\"}");
        } catch (SQLException e) {
        	System.out.println("i'm here in update catch");
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"Failed to update profile\"}");
        } finally {
            try {
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
