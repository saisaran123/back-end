package com.kunto.controller;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.PostDAO;
import com.kunto.models.UserLikes;
@WebServlet("/mostLikedPosters")
public class MostLikedPostersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try {
           
            PostDAO dao = PostDAO.getInstance();
            List<UserLikes> mostLiked = dao.getMostLikedPosters(5);

            JSONArray jsonArray = new JSONArray();
            for (UserLikes user : mostLiked) {
                JSONObject userJson = new JSONObject();
                userJson.put("userId", user.getUserId());
                userJson.put("username", user.getUsername());
                userJson.put("count", user.getTotalLikes());
                jsonArray.put(userJson);
            }

            JSONObject data = new JSONObject();
            data.put("success", true);
            data.put("data", jsonArray);
            // Write JSON response
            PrintWriter out = response.getWriter();
            out.print(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
