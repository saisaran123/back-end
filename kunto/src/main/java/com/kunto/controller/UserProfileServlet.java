package com.kunto.controller;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.config.DBConnection;

@WebServlet("/userProfile")
public class UserProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Assuming user_id is passed as a parameter
        HttpSession session=request.getSession(false);

    	int userId=(int) session.getAttribute("user_id");


        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establish connection
        	conn = DBConnection.getInstance().getConnection();
            // SQL query to fetch user profile data
            String query = "SELECT user_name, height, weight, user_profile_picture, date_of_birth ,active_level FROM user_details WHERE user_id = ?";
            System.out.println("dei enna da unaku");
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);  // assuming user_id is passed as an integer
            rs = stmt.executeQuery();
            String query2 = "SELECT  email,about FROM users WHERE user_id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(query2);
            stmt2.setInt(1, userId);  // assuming user_id is passed as an integer
            ResultSet rs2 = stmt2.executeQuery();
            System.out.println("vandhutan da maple");
            if (rs.next() && rs2.next()) {
                // Prepare JSON response
            	System.out.println("heiieieie");
                JSONObject userProfile = new JSONObject();
                userProfile.put("user_name", rs.getString("user_name"));
                userProfile.put("height", rs.getInt("height"));
                userProfile.put("weight", rs.getInt("weight"));
                userProfile.put("user_profile_picture", rs.getString("user_profile_picture"));
                userProfile.put("active_level", rs.getString("active_level"));
                userProfile.put("email", rs2.getString("email"));
                userProfile.put("about", rs2.getString("about"));
                // Calculate age based on date_of_birth
                Date dob = rs.getDate("date_of_birth");
                int age = calculateAge(dob);
                userProfile.put("age", age);

                // Send the JSON response
                PrintWriter out = response.getWriter();
                out.println(userProfile);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"Unable to fetch user profile\"}");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int calculateAge(Date dob) {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(dob);

        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // Check if the user hasn't had their birthday this year
        if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) || 
            (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }
}
