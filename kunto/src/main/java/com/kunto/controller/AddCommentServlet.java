
package com.kunto.controller;

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

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.PostDAO;
import com.kunto.models.*;


//@WebServlet("/addComment")
//public class CommentServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        StringBuilder sb = new StringBuilder();
//        String line;
//        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//
//        String requestData = sb.toString();
//        try {
//            JSONObject json = new JSONObject(requestData);
//            int postId = json.getInt("id");
//            int userId = json.getInt("userId");
//            String comment = json.getString("reply");
//
//            Connection conn = Database.getInstance().getConnection();
//            String query = "INSERT INTO comments (post_id, user_id, comment) VALUES (?, ?, ?)";
//            try (PreparedStatement stmt = conn.prepareStatement(query)) {
//                stmt.setInt(1, postId);
//                stmt.setInt(2, userId);
//                stmt.setString(3, comment);
//                stmt.executeUpdate();
//
//                JSONObject responseJson = new JSONObject();
//                responseJson.put("status", "success");
//                response.setContentType("application/json");
//                response.getWriter().write(responseJson.toString());
//            } catch (SQLException e) {
//                e.printStackTrace();
//                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
//}


@WebServlet("/addComment")
public class AddCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String requestData = sb.toString();
        try {
            JSONObject json = new JSONObject(requestData);
            int postId = json.getInt("id");
            //int userId = json.getInt("userId");
            HttpSession session=request.getSession(false);
            int userId = (int) session.getAttribute("user_id");
            System.out.println(session);
            System.out.println(userId);

            
            String commentText = json.getString("reply");
            

            Comment comment = new Comment(postId, userId, commentText);
            PostDAO postDAO = PostDAO.getInstance();
            boolean success = postDAO.addComment(comment);

            JSONObject responseJson = new JSONObject();
            responseJson.put("status", success ? "success" : "failure");
            response.setContentType("application/json");
            response.getWriter().write(responseJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
} 

