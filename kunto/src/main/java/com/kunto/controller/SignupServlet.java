package com.kunto.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.dao.UserDAO;
import com.kunto.util.PasswordUtil;











@WebServlet("/Signup")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        System.out.println("Signup processing...");
        PrintWriter out = response.getWriter();

        StringBuilder data = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            data.append(line);
        }

        try {
            JSONObject json = new JSONObject(data.toString());
            String email = json.getString("email");
            String username = json.getString("name");
            String password = json.getString("password");

            System.out.println("Received Data - Username: " + username + ", Email: " + email);

            UserDAO userDAO = UserDAO.getInstance();
            int userId = userDAO.insertUser(email, username, password); 

            JSONObject jsonResponse = new JSONObject();
            if (userId != -1) {
                userDAO.insertUserStarHistory(userId); 
                userDAO.addUserToCommonGroups(userId);
                HttpSession session = request.getSession();
                session.setAttribute("user_id",userId);
                session.setAttribute("username",username);
                System.out.println(userId);
                session.setMaxInactiveInterval(24*24*60);
                response.sendRedirect("details.html");
               // response.sendRedirect("getUserDetails.html"); 
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Signup failed. Please try again.");
                out.print(jsonResponse.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred during signup.");
            out.print(errorResponse.toString());
        }
    }
}

