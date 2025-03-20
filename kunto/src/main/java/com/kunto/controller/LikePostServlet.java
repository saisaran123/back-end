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

import com.kunto.dao.PostDAO;
import com.kunto.models.Like;
//
//@WebServlet("/like")
//public class AddorRemoveLikeServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	System.out.println("handlelike");
//        StringBuilder sb = new StringBuilder();
//        BufferedReader reader = request.getReader();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//
//        JSONObject jsonData = new JSONObject(sb.toString());
//        System.out.println(jsonData);
//        int userId = jsonData.getInt("userId");
//        int postId = jsonData.getInt("postId");
//
//        Connection conn = Database.getInstance().getConnection();
//        PrintWriter out = response.getWriter();
//
//        try {
//            // Check if like already exists
//            String checkQuery = "SELECT 1 FROM likes WHERE user_id = ? AND post_id = ?";
//            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
//            checkStmt.setInt(1, userId);
//            checkStmt.setInt(2, postId);
//            ResultSet rs = checkStmt.executeQuery();
//
//            if (rs.next()) {
//                // Like exists, remove it
//                String deleteQuery = "DELETE FROM likes WHERE user_id = ? AND post_id = ?";
//                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
//                deleteStmt.setInt(1, userId);
//                deleteStmt.setInt(2, postId);
//                deleteStmt.executeUpdate();
//                JSONObject json = new JSONObject();
//                json.put("liked", false);
//                out.print(json); // Indicate like removed
//            } else {
//                // Like doesn't exist, add it
//                String insertQuery = "INSERT INTO likes (user_id, post_id) VALUES (?, ?)";
//                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
//                insertStmt.setInt(1, userId);
//                insertStmt.setInt(2, postId);
//                insertStmt.executeUpdate();
//                JSONObject json = new JSONObject();
//                json.put("liked", true);
//                out.print(json);
//               ; // Indicate like added
//            }
//        } catch (SQLException e) {
//        	JSONObject json = new JSONObject();
//            json.put("status", "error");
//            e.printStackTrace();
//            out.println(json);
//        } finally {
//            out.close();
//        }
//    }
//}
//




@WebServlet("/like")
public class LikePostServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PostDAO postDAO = PostDAO.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        StringBuilder sb = new StringBuilder();
      BufferedReader reader = request.getReader();
      String line;
      while ((line = reader.readLine()) != null) {
          sb.append(line);
      }

      JSONObject jsonData = new JSONObject(sb.toString());
      System.out.println(jsonData);
     // int userId = jsonData.getInt("userId");
      HttpSession session=request.getSession(false);
      int userId = (int) session.getAttribute("user_id");
      int postId = jsonData.getInt("postId");
        
        Like like = new Like();
        like.setPostId(postId);
        like.setUserId(userId);

        boolean isLiked = postDAO.toggleLike(like);
        response.setContentType("application/json");
        response.getWriter().write("{\"liked\": " + isLiked + "}");
    }
}

