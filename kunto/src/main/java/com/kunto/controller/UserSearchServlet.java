package com.kunto.controller;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

/**
 * Servlet implementation class SearchUserServlet
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.config.DBConnection;

@WebServlet("/searchUser")
public class UserSearchServlet extends HttpServlet {
	

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println("USER");

        String searchQuery = request.getParameter("query");
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        JSONArray usersArray = new JSONArray();
        Map<String, Session> userSessionMap = ChatServer.getUserSessionMap();
        try{
        	
        	Connection conn =DBConnection.getInstance().getConnection();

            PreparedStatement stmt = conn.prepareStatement("SELECT user_id, username, about FROM users WHERE username ILIKE ? OR email ILIKE ? limit 10");

            String searchPattern = "%" + searchQuery + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	String username=rs.getString("username");
                JSONObject userObj = new JSONObject();
                if (userSessionMap.containsKey(username)) {
                   userObj.put("online", true);
                }else {
                	userObj.put("online", false);
                }
                userObj.put("id", "u"+rs.getInt("user_id"));
                userObj.put("name", username);
                userObj.put("about", rs.getString("about"));
                userObj.put("type","user");
                usersArray.put(userObj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();
        out.print(usersArray.toString());
        out.flush();
    }
}
