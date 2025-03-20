package com.kunto.controller;

import com.kunto.config.DBConnection;
import com.kunto.dao.UserDetailsDao;
import com.kunto.model.User;
import com.kunto.model.user_details;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

@WebServlet("/registerUser")
public class UserRegistrationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("user_id");
        try {
            // Read JSON data from request
        	Connection conn = DBConnection.getInstance().getConnection();
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonData = new JSONObject(sb.toString());

            // Create User object
            user_details user = new user_details();
            user.setGoal(jsonData.optString("goal", ""));
            user.setHeight(jsonData.getInt("height"));
            user.setWeight(jsonData.getInt("weight"));
            user.setLevel(jsonData.optString("level", ""));
            user.setActiveLevel(jsonData.optString("activeLevel", ""));
            user.setGender(jsonData.optString("gender", ""));
            user.setDateOfBirth(jsonData.optString("dateOfBirth", null));
            user.setCountry(jsonData.optString("country", ""));
            user.setCity(jsonData.optString("city", ""));
            
            String username = null;
            String query = "SELECT username FROM users WHERE user_id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString("username");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception properly in production
            }
            
            user.setUsername(username);
            user.setId(userId);

            // Register user using DAO
            UserDetailsDao userDAO = new UserDetailsDao();
            boolean isRegistered = userDAO.registerUser(user);
            int age = calculateAge(jsonData.getString("dateOfBirth"));
            if (isRegistered) {
            	NutritionServlet.getGoal(userId,age,jsonData.getString("gender"),jsonData.getInt("weight"),jsonData.getInt("height"),jsonData.getString("activeLevel"),jsonData.getString("goal"));
                out.write("{\"status\": \"success\"}");
                
            } else {
                out.write("{\"status\": \"error\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"status\": \"error\"}");
        }
    }
    public static int calculateAge(String dobString) {
        // Define the formatter (matches "yyyy-MM-dd")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Convert String to LocalDate
        LocalDate dateOfBirth = LocalDate.parse(dobString, formatter);
        
        // Calculate age
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

 
}
