package com.kunto.controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.config.DBConnection;

@WebServlet("/getuserid")
public class GetUserIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//System.out.println(request.getParameter("username"));
		String username = request.getParameter("username");
		
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String query = "select user_id from users where username = ?;";
		

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			
			

			ResultSet rs = stmt.executeQuery();
			JSONObject userIdObject = new JSONObject();
			
			while (rs.next()) {
				
				userIdObject.put("user_id", rs.getInt("user_id"));

			}

			response.setContentType("application/json");
			response.getWriter().write(userIdObject.toString());

		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}

	
	
}
