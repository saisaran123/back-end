package com.kunto.controller;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.UUID;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.Part;
//
//import org.json.JSONObject;
//
//import com.kunto.util.Database;
//
//@WebServlet("/createPost")
//
//
//public class CreatePostServlet extends HttpServlet {
//
//   
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // Get form data
//    	StringBuilder sb = new StringBuilder();
//      String line;
//      BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
//      while ((line = reader.readLine()) != null) {
//          sb.append(line);
//      }
//      String requestData = sb.toString();
//
//      
//        JSONObject json = new JSONObject(requestData);
//        int id = json.getInt("userId");
//      String content = json.getString("content");
//      String imageUrl = json.optString("imageUrl", null);
//        String title = request.getParameter("title");
//       
//      
//
//        // Validate required fields
//        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("{\"error\": \"Title and content are required.\"}");
//            return;
//        }
//
//               
//
//        // Save the post to the database
//        try (Connection conn = Database.getInstance().getConnection()) {
//            String sql = "INSERT INTO posts (title, content, image_url, user_id) VALUES (?, ?, ?, ?)";
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, title);
//                stmt.setString(2, content);
//                stmt.setString(3, imageUrl);
//                stmt.setInt(4, id); // Replace with the actual user ID (e.g., from session)
//
//                int rowsInserted = stmt.executeUpdate();
//                if (rowsInserted > 0) {
//                    response.setStatus(HttpServletResponse.SC_OK);
//                    response.getWriter().write("{\"success\": true, \"message\": \"Post created successfully.\"}");
//                } else {
//                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                    response.getWriter().write("{\"error\": \"Failed to create post.\"}");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
//        }
//    }
//}




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.kunto.models.Post;

//@WebServlet("/createPost")
//public class CreatePostServlet extends HttpServlet {
//
//    
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // Read the JSON payload from the request
//        StringBuilder sb = new StringBuilder();
//        String line;
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
//        }
//
//        // Parse the JSON payload
//        JSONObject json = new JSONObject(sb.toString());
//        String title = json.optString("title", null);
//        int userId = json.optInt("userId", -1);
//        String content = json.optString("content", null);
//        String imageUrl = json.optString("imageUrl","");
//
//        // Validate required fields
//        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty() || userId == -1) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("{\"error\": \"Title, content, and userId are required.\"}");
//            return;
//        }
//
//        // Save the post to the database and retrieve the generated key
//        try (Connection conn = Database.getInstance().getConnection()) {
//            String sql = "INSERT INTO posts (title, content, image_url, user_id) VALUES (?, ?, ?, ?)";
//            
//            // Use PreparedStatement.RETURN_GENERATED_KEYS to retrieve the auto-incremented key
//            try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//                stmt.setString(1, title);
//                stmt.setString(2, content);
//                stmt.setString(3, imageUrl);
//                stmt.setInt(4, userId);
//
//                int rowsInserted = stmt.executeUpdate();
//                if (rowsInserted > 0) {
//                    // Retrieve the generated key
//                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                        if (generatedKeys.next()) {
//                            int postId = generatedKeys.getInt(1); // Get the auto-incremented post_id
//
//                            // Return the postId in the response
//                            response.setStatus(HttpServletResponse.SC_OK);
//                            response.getWriter().write("{\"success\": true, \"message\": \"Post created successfully.\", \"postId\": " + postId + "}");
//                        } else {
//                            // No generated key found
//                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                            response.getWriter().write("{\"error\": \"Failed to retrieve generated post ID.\"}");
//                        }
//                    }
//                } else {
//                    // No rows inserted
//                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                    response.getWriter().write("{\"error\": \"Failed to create post.\"}");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
//        }
//    }



@WebServlet("/createPost")
public class CreatePostServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JSONObject json = new JSONObject(sb.toString());
        String title = json.optString("title", null);
       // int userId = json.optInt("userId", -1);
        HttpSession session=request.getSession(false);
        int userId = (int) session.getAttribute("user_id");
        String content = json.optString("content", null);
        String imageUrl = json.optString("imageUrl", "");
        System.out.println(json);

        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty() || userId == -1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Title, content, and userId are required.\"}");
            return;
        }

        Post post = new Post(userId,content,title,imageUrl);
        PostDAO postDAO = PostDAO.getInstance();

        try {
            int postId = postDAO.createPost(post);
            if (postId != -1) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true, \"message\": \"Post created successfully.\", \"id\": " + postId + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Failed to create post.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        }
    }
} 

//}