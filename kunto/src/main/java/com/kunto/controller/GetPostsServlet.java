package com.kunto.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.JoinRowSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.PostDAO;
import com.kunto.models.Post;
//
//@WebServlet("/getPosts")
//public class GetPostsServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        int from=Integer.parseInt(request.getParameter("from"));
//        int to=Integer.parseInt(request.getParameter("to"));
//        System.out.println("getposts" +from +" "+to);
//        try (Connection conn = Database.getInstance().getConnection()) {
////            String query = "SELECT p.id, p.user_id, p.content,p.title, p.image_url, p.created_at, u.username " +
////                           "FROM posts p INNER JOIN users u ON p.user_id = u.id " +
////                           "ORDER BY p.created_at DESC";
//        	String totalQuery="select max(id) as max from posts";
//        	
//        	String query = "SELECT p.id, p.user_id, p.content, p.title, p.image_url, p.created_at, u.username " +
//                  "FROM posts p INNER JOIN users u ON p.user_id = u.id " +
//                  "ORDER BY p.created_at DESC LIMIT ? OFFSET ?";
//   
//            try {
//            	PreparedStatement tstmt = conn.prepareStatement(totalQuery);
//            	ResultSet r = tstmt.executeQuery();
//            	r.next();
//            	int totalPosts=r.getInt("max");
//            	PreparedStatement stmt = conn.prepareStatement(query);
//            	stmt.setInt(1, (to - from + totalPosts) % totalPosts);
//            	  stmt.setInt(2, from % totalPosts);
//            	
//            	
//                 ResultSet rs = stmt.executeQuery();
//
//                JSONArray postsArray = new JSONArray();
//
//                while (rs.next()) {
//                    JSONObject postJson = new JSONObject();
//                    postJson.put("id", rs.getInt("id"));
//                    postJson.put("userId", rs.getInt("user_id"));
//                    postJson.put("username", rs.getString("username"));
//                    postJson.put("content", rs.getString("content"));
//                    postJson.put("title", rs.getString("title"));
//                    postJson.put("imageUrl", rs.getString("image_url"));
//                    postJson.put("createdAt", rs.getTimestamp("created_at").toString());
//
//                    postsArray.put(postJson);
//                }
//
//                JSONObject responseJson = new JSONObject();
//                responseJson.put("status", "success");
//                responseJson.put("posts", postsArray);
//
//                response.getWriter().write(responseJson.toString());
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//
//            JSONObject errorJson = new JSONObject();
//            errorJson.put("status", "error");
//            errorJson.put("message", "Database error: " + e.getMessage());
//            response.getWriter().write(errorJson.toString());
//        }
//   } catch (SQLException e1) {
//	// TODO Auto-generated catch block
//	e1.printStackTrace();
//}
// }
//}



//GetPostsServlet.java
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/getPosts")
public class GetPostsServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;


 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     response.setContentType("application/json");
     response.setCharacterEncoding("UTF-8");

     int from = Integer.parseInt(request.getParameter("from"));
     int to = Integer.parseInt(request.getParameter("to"));

     PostDAO postDAO = PostDAO.getInstance();
	 List<Post> posts = postDAO.getPosts(from, to);
	 JSONArray postsArray = new JSONArray();

	 for (Post post : posts) {
	     JSONObject postJson = new JSONObject();
	     postJson.put("id", post.getId());
	     postJson.put("userId", post.getUserId());
	     postJson.put("username", post.getUsername());
	     postJson.put("content", post.getContent());
	     postJson.put("title", post.getTitle());
	     postJson.put("imageUrl", post.getImageUrl());
	     postJson.put("views", post.getViews());
	     postJson.put("createdAt", post.getCreatedAt());

	     postsArray.put(postJson);
	 }

	 JSONObject responseJson = new JSONObject();
	 responseJson.put("status", "success");
	 responseJson.put("posts", postsArray);

	 response.getWriter().write(responseJson.toString());
 }
}



