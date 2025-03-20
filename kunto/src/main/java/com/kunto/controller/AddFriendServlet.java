package com.kunto.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import com.kunto.dao.UserDAO;

@WebServlet("/addFriend")
public class AddFriendServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	 StringBuilder sb = new StringBuilder();
         String line;
         BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
         while ((line = reader.readLine()) != null) {
             sb.append(line);
         }

         String requestData = sb.toString();
         JSONObject json = new JSONObject(requestData);
      
        //int userId = json.getInt("userId");
         HttpSession session=request.getSession(false);
         int userId = (int) session.getAttribute("user_id");
        int friendId =json.getInt("friendId");
        UserDAO userDAO =UserDAO.getInstance();
        
        boolean friendAdded=userDAO.addFriend(userId,friendId);
        response.setContentType("application/json");
        if(friendAdded) {
        	 response.getWriter().write("{\"status\":\"success\",\"message\":\"Friend added successfully\"}");
        }else {
        	 response.getWriter().write("{\"status\":\"error\",\"message\":\"Failed to add friend\"}");
        }
		
    }
}
