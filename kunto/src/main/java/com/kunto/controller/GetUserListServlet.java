package com.kunto.controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.websocket.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.UserDAO;

////**
// * Servlet implementation class GetUserListServlet
// */
//@WebServlet("/GetUserListServlet")
//public class GetUserListServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//     
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		 int userId = Integer.parseInt(request.getParameter("userId"));
//	      
//
//	        Connection conn = null;
//			try {
//				conn = Database.getConnection();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        String query = "SELECT DISTINCT \n"
//	        		+ "    CASE \n"
//	        		+ "        WHEN sender_id = ? THEN receiver_id   -- If sender_id is 1, return receiver_id\n"
//	        		+ "        ELSE sender_id                        -- Otherwise, return sender_id\n"
//	        		+ "    END AS chat_user_id\n"
//	        		+ "FROM chats\n"
//	        		+ "WHERE sender_id = ? OR receiver_id = ?;";
//	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
//	            stmt.setInt(1, userId);
//	           
//
//	            ResultSet rs = stmt.executeQuery();
//	            List<Integer> list =new ArrayList<>();
//	            while (rs.next()) {
//	                list.add(rs.getInt("chat_user_id"));
//	            }
//	            
//	            JSONArray messagesArray = new JSONArray();
//
//	            while (rs.next()) {
//	                JSONObject message = new JSONObject();
//	                message.put("sender", rs.getInt("sender_id"));
//	                message.put("message", rs.getString("message"));
//	                message.put("timestamp", rs.getString("timestamp"));
//	                messagesArray.put(message);
//	            }
//
//	            response.setContentType("application/json");
//	            response.getWriter().write(messagesArray.toString());
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//	        }
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}
//
//}

//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.websocket.Session;
//
//import org.apache.catalina.valves.rewrite.Substitution.StaticElement;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
///**
// * Servlet implementation class GetUserListServlet
// */
//@WebServlet("/GetUserListServlet")
//public class GetUserListServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println(request.getParameter("userId"));
//		int userId = Integer.parseInt(request.getParameter("userId"));
//		
//		Connection conn = null;
//		try {
//			conn = Database.getInstance().getConnection();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		// Modified query to join with the users table and get both user ID and username
//		String query = "SELECT DISTINCT u.id AS chat_user_id, u.username ,u.about\n"
//				+ "FROM chats c\n"
//				+ "JOIN users u ON (u.id = c.sender_id OR u.id = c.receiver_id)\n"
//				+ "WHERE c.sender_id = ? OR c.receiver_id = ? ;";
//		 String q="SELECT DISTINCT \n"
//         		+ "    CASE \n"
//         		+ "        WHEN sender_id = ? THEN receiver_id\n"
//         		+ "        ELSE sender_id\n"
//         		+ "    END AS chat_user_id\n"
//         		+ "FROM chats\n"
//         		+ "WHERE sender_id = ? OR receiver_id = ?; ";
//		 
//		 
//		
//		Map<String, Session> userSessionMap =ChatServer.getUserSessionMap();
//	
//		try (PreparedStatement stmt = conn.prepareStatement(query)) {
//			stmt.setInt(1, userId);
//			stmt.setInt(2, userId); 
//			
//
//			ResultSet rs = stmt.executeQuery();
//			JSONArray userListArray = new JSONArray();
//
//			while (rs.next()) {
//				
//				JSONObject userObject = new JSONObject();
//				String username=rs.getString("username");
//				if(userSessionMap.containsKey(username)) {
//					Session targetSession=userSessionMap.get(username);
//					userObject.put("online",true);
//					JSONObject updateOnline = new JSONObject();
//					updateOnline.put("type", "updateOnline");
//					updateOnline.put("name", username);
//					targetSession.getBasicRemote().sendText(response.toString());
//				}else {
//					userObject.put("online",false);
//				}
//				userObject.put("id", "u"+rs.getInt("chat_user_id"));
//				userObject.put("name",username);
//				userObject.put("about", rs.getString("about"));
//				userObject.put("type", "user");
//				
//				
//				
//				
//				userListArray.put(userObject);
//			}
//
//			// Send the response
//			response.setContentType("application/json");
//			response.getWriter().write(userListArray.toString());
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//		}
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
//	}
//}
//




//@WebServlet("/GetUserListServlet")
//public class GetUserListServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int userId = Integer.parseInt(request.getParameter("userId"));
//        
//        Connection conn = null;
//        conn = Database.getInstance().getConnection();
//
//        // Query to get both directions of the friendship from the user_friends table
//        String query = "SELECT u.id AS friend_id, u.username, u.about "
//                     + "FROM user_friends uf "
//                     + "JOIN users u ON u.id = (CASE WHEN uf.user_id = ? THEN uf.friend_id ELSE uf.user_id END) "
//                     + "WHERE uf.user_id = ? OR uf.friend_id = ?";
//
//        Map<String, Session> userSessionMap = ChatServer.getUserSessionMap();
//
//        try (PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setInt(1, userId);
//            stmt.setInt(2, userId);  // For both directions
//            stmt.setInt(3, userId);  // For both directions
//
//            ResultSet rs = stmt.executeQuery();
//            JSONArray userListArray = new JSONArray();
//
//            while (rs.next()) {
//                JSONObject userObject = new JSONObject();
//                String username = rs.getString("username");
//                String id="u" + rs.getInt("friend_id");
//                // Check if the user is online
//                if (userSessionMap.containsKey(username)) {
//                    Session targetSession = userSessionMap.get(username);
//                    userObject.put("online", true);
//                    
//                    // Notify other users about the user's online status
//                    JSONObject updateOnline = new JSONObject();
//                    updateOnline.put("type", "updateOnline");
//                   // updateOnline.put("name", username);
//                    updateOnline.put("id", "u"+userId);
//                    updateOnline.put("online", true);
//                    targetSession.getBasicRemote().sendText(updateOnline.toString());
//                } else {
//                    userObject.put("online", false);
//                }
//
//                // Add friend details to response
//                userObject.put("id", id);
//                userObject.put("name", username);
//                userObject.put("about", rs.getString("about"));
//                userObject.put("type", "user");
//
//                userListArray.put(userObject);
//            }
//
//            // Send the response as JSON
//            response.setContentType("application/json");
//            response.getWriter().write(userListArray.toString());
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doGet(request, response);
//    }
//}



import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

@WebServlet("/GetUserListServlet")
public class GetUserListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //  int userId = Integer.parseInt(request.getParameter("userId"));
    	 HttpSession session=request.getSession(false);
         int userId = (int) session.getAttribute("user_id");

        UserDAO userDAO = UserDAO.getInstance();
        JSONArray userListArray;

        userListArray = userDAO.getUserFriends(userId,ChatServer.getUserSessionMap());
		response.setContentType("application/json");
		response.getWriter().write(userListArray.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

