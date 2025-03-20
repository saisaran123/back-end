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
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.config.DBConnection;

/**
 * Servlet implementation class GetGroupListServlet
 */
@WebServlet("/GetGroupList")
public class GetGroupListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//int userId = Integer.parseInt(request.getParameter("userId"));
		 HttpSession session=request.getSession(false);
         int userId = (int) session.getAttribute("user_id");
		
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String query = "SELECT u.group_id, g.group_name \n"
				+ "FROM groups g\n"
				+ "JOIN user_groups u ON u.group_id = g.id\n"
				+ "WHERE u.user_id = ?";
		

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, userId);
			
			

			ResultSet rs = stmt.executeQuery();
			JSONArray groupListArray = new JSONArray();

			while (rs.next()) {
				
				JSONObject groupObject = new JSONObject();
				 groupObject.put("id", "g"+rs.getInt("group_id"));
				 groupObject.put("name", rs.getString("group_name"));
				 groupObject.put("type","group");
				
				
				groupListArray.put(groupObject);
			}

			response.setContentType("application/json");
			response.getWriter().write(groupListArray.toString());

		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
