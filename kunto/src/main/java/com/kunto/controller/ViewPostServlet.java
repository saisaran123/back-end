package com.kunto.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.dao.PostDAO;


@WebServlet("/viewPost")
public class ViewPostServlet extends HttpServlet {

    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	  StringBuilder sb = new StringBuilder();
          String line;
          BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
          while ((line = reader.readLine()) != null) {
              sb.append(line);
          }

          String requestData = sb.toString();
          
              JSONObject json = new JSONObject(requestData);
              int postId = json.getInt("post_id");
             // int userId = json.getInt("user_id");
              HttpSession session=request.getSession(false);
              int userId = (int) session.getAttribute("user_id");
     
         PostDAO postDAO = PostDAO.getInstance();
        boolean alreadyViewed = postDAO.hasUserViewedPost(postId, userId);

        if (!alreadyViewed) {
            postDAO.incrementViewCount(postId, userId);
        }

        int totalViews = postDAO.getPostViewCount(postId);

        response.setContentType("application/json");
        response.getWriter().write("{\"views\": " + totalViews + "}");
    }
}