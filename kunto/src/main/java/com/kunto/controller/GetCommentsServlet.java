
package com.kunto.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.PostDAO;
import com.kunto.models.Comment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

//@WebServlet("/getComments")
//public class GetCommentsServlet extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	 StringBuilder sb = new StringBuilder();
//         String line;
//         BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
//         while ((line = reader.readLine()) != null) {
//             sb.append(line);
//         }
//
//         String requestData = sb.toString();
//        
//         JSONObject json = new JSONObject(requestData);
//         String postIdParam= json.getInt("postId") +"";
//
//      
//        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//
//        if (postIdParam == null || postIdParam.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            JSONObject errorJson = new JSONObject();
//            errorJson.put("status", "error");
//            errorJson.put("message", "Missing post_id parameter");
//            out.print(errorJson);
//            return;
//        }
//
//        int postId;
//        try {
//            postId = Integer.parseInt(postIdParam);
//        } catch (NumberFormatException e) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            JSONObject errorJson = new JSONObject();
//            errorJson.put("status", "error");
//            errorJson.put("message", "post id not a number.");
//            out.print(errorJson);
//            return;
//        }
//
//        Connection conn = Database.getInstance().getConnection();
//        
//        
//        String query = "SELECT c.id, c.user_id, u.username, c.comment, c.created_at " +
//                "FROM comments c " +
//                "JOIN users u ON c.user_id = u.id " +
//                "WHERE c.post_id = ? " +
//                "ORDER BY c.created_at";
//
// 
//        
//        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.setInt(1, postId); // Set the post_id parameter
//            ResultSet rs = pstmt.executeQuery();
//            JSONArray commentsArray = new JSONArray();
//            while (rs.next()) {
//           	 
//           	 JSONObject comment = new JSONObject();
//                comment.put("id", rs.getInt("id"));
//                comment.put("username", rs.getString("username"));
//                comment.put("user_id", rs.getInt("user_id"));
//                comment.put("comment", rs.getString("comment"));
//                comment.put("created_at", rs.getTimestamp("created_at").toString());
//                commentsArray.put(comment);
//                
//
//               
//            }
//        
//
//
//            JSONObject responseObject = new JSONObject();
//            responseObject.put("comments", commentsArray);
//
//            out.print(responseObject.toString());
//            response.setStatus(HttpServletResponse.SC_OK);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            JSONObject errorJson = new JSONObject();
//            errorJson.put("status", "error");
//            errorJson.put("message", "Database erro");
//            out.print(errorJson);
//           
//        }
//    }
//}




@WebServlet("/getComments")
public class GetCommentsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	StringBuilder sb = new StringBuilder();
    	String line;
      BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
      while ((line = reader.readLine()) != null) {
          sb.append(line);
      }

      String requestData = sb.toString();
     
      JSONObject json = new JSONObject(requestData);
     
        int postId = json.getInt("postId");

        PostDAO postDAO = PostDAO.getInstance();
        try {
            List<Comment> comments = postDAO.getComments(postId);
            JSONArray commentsArray = new JSONArray();

            for (Comment comment : comments) {
                JSONObject commentJson = new JSONObject();
                commentJson.put("userId", comment.getUserId());
                commentJson.put("comment", comment.getComment());
                commentJson.put("username", comment.getUserName());
                commentJson.put("createAt", comment.getCreateAt());
                commentsArray.put(commentJson);
            }
            
          JSONObject responseObject = new JSONObject();
          responseObject.put("comments", commentsArray);

          response.getWriter().print(responseObject.toString());


           

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

