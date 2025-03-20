package com.kunto.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.FAQDao;
import com.kunto.models.TrendingUser;


@WebServlet("/trendingUsersFaq")
public class TrendingUsersServletaq extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	System.out.println("jkkjkhkjh");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            FAQDao dao = FAQDao.getInstance();
            List<TrendingUser> trendingUsers = dao.getTrendingUsers(5);

            JSONArray jsonArray = new JSONArray();
            for (TrendingUser user : trendingUsers) {
                JSONObject userObj = new JSONObject();
                userObj.put("userId", user.getUserId());
                userObj.put("username", user.getUsername());
                userObj.put("count", user.getEngagementScore());
                jsonArray.put(userObj);
            }
            JSONObject data = new JSONObject();
            data.put("success", true);
            data.put("data", jsonArray);
            // Write JSON response
            
            out.print(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}