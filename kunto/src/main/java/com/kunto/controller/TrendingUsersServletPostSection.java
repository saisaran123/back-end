package com.kunto.controller;


import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.PostDAO;
import com.kunto.models.TrendingUser;
@WebServlet("/trendingUsersforPosts")
public class TrendingUsersServletPostSection extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
          
            PostDAO dao = PostDAO.getInstance();
            List<TrendingUser> trendingUsers = dao.getTrendingUsers(5);

            JSONArray jsonArray = new JSONArray();
            for (TrendingUser user : trendingUsers) {
                JSONObject userJson = new JSONObject();
                userJson.put("userId", user.getUserId());
                userJson.put("username", user.getUsername());
                userJson.put("count", user.getEngagementScore());
                jsonArray.put(userJson);
            }
            JSONObject data=new JSONObject();
            data.put("success", true);
            data.put("data", jsonArray);

            out.print(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
