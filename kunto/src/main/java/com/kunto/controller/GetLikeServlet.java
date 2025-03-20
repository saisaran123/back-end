package com.kunto.controller;

import java.io.IOException;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.PostDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
//@WebServlet("/getLike")
//public class GetLikeServlet extends HttpServlet {
//	//Connection conn = null;
//	
//	public void init() {
//		
//		//	Connection conn = Database.getInstance().getConnection();
//		
//	}
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // Retrieve postId and userId from the request (e.g., as query parameters)
//        int postId = Integer.parseInt(request.getParameter("postId"));
//        int userId = Integer.parseInt(request.getParameter("userId"));
//        System.out.println(userId +"    "+postId);
//        String query = """
//            SELECT COUNT(*) AS total_likes,
//                   EXISTS (
//                       SELECT 1 FROM likes l2 
//                       WHERE l2.post_id = ? AND l2.user_id = ?
//                   ) AS user_liked,
//                   json_agg(u.username) AS liked_users
//            FROM likes l
//            JOIN users u ON l.user_id = u.id
//            WHERE l.post_id = ?;
//        """;
//       // System.out.println(conn);
//        try {
//        	Connection conn = Database.getInstance().getConnection();// Assuming you have a method to get the DB connection
//             PreparedStatement pstmt = conn.prepareStatement(query);
//
//            pstmt.setInt(1, postId);
//            pstmt.setInt(2, userId);
//            pstmt.setInt(3, postId);
//
//            ResultSet rs = pstmt.executeQuery();
//            
//              JSONObject result = new JSONObject();
//            if (rs.next()) {
//            	 System.out.println(rs.getString("liked_users"));
//
//                if(rs.getString("liked_users") == null) {
//                	result.put("likedUsers", false);
//                }else {
//                	 result.put("likedUsers", new JSONArray(rs.getString("liked_users")));
//                }
//                result.put("totalLikes", rs.getInt("total_likes"));
//                result.put("userLiked", rs.getBoolean("user_liked"));
//               
//            }
//            
//            // Set response type and write the result
//            response.setContentType("application/json");
//            response.getWriter().println(result);
//            
//        } catch (SQLException e) {
//            e.printStackTrace(); // Log error (or handle it as needed)
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }
//
//   
//}



import java.io.IOException;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/getLike")
public class GetLikeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int postId = Integer.parseInt(request.getParameter("postId"));
     //   int userId = Integer.parseInt(request.getParameter("userId"));
        HttpSession session=request.getSession(false);
        int userId = (int) session.getAttribute("user_id");
        System.out.println(userId + "    " + postId);

        PostDAO postDAO = PostDAO.getInstance();
        try {
            JSONObject result = postDAO.getLikeDetails(postId, userId);

            response.setContentType("application/json");
            response.getWriter().println(result);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
} 
