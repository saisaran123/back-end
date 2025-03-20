package com.kunto.controller;

import com.kunto.dao.UserDao;
import com.kunto.model.User;
import com.kunto.model.user_details;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

@WebServlet("/registerUser")
public class UserRegistrationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // Read JSON data from request
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
            user.setUsername(jsonData.optString("username", ""));
            user.setProfileImage(jsonData.optString("profileImage", ""));

            // Register user using DAO
            UserDao userDAO = new UserDao();
            boolean isRegistered = userDAO.registerUser(user);

            if (isRegistered) {
                out.write("{\"status\": \"success\"}");
            } else {
                out.write("{\"status\": \"error\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"status\": \"error\"}");
        }
    }
}
