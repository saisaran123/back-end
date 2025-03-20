package com.kunto.controller;

//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.json.JSONArray;
//
//import com.kunto.dao.PostDAO;
//import com.kunto.model.LeaderboardEntry;
//@WebServlet("/postCountLeaderBoard")
//public class LeaderboardServlet extends HttpServlet {
//    private PostDAO postDAO;
//
//    @Override
//    public void init() throws ServletException {
//        
//        postDAO = PostDAO.getInstance();
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//      
//        List<LeaderboardEntry> leaderboard = postDAO.getTopUsersByPostCount(5);
//
//        
//        StringBuilder jsonResponse = new StringBuilder("[");
//        
//        
//        for (int i = 0; i < leaderboard.size(); i++) {
//            LeaderboardEntry entry = leaderboard.get(i);
//            jsonResponse.append("{")
//            			.append("\"username\":").append(entry.getUserName()).append(",")
//                        .append("\"userId\":").append(entry.getUserId()).append(",")
//                        .append("\"postCount\":").append(entry.getPostCount())
//                        .append("}");
//            if (i < leaderboard.size() - 1) {
//                jsonResponse.append(",");
//            }
//        }
//        jsonResponse.append("]");
//
//        // Set response content type to JSON
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        // Write JSON response
//        PrintWriter out = response.getWriter();
//        out.print(jsonResponse.toString());
//        
//    }
//}




import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.PostDAO;
import com.kunto.models.LeaderboardEntry;

@WebServlet("/postCountLeaderBoard")
public class LeaderboardServlet extends HttpServlet {
    private PostDAO postDAO;

    @Override
    public void init() throws ServletException {
        postDAO = PostDAO.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch leaderboard data
        List<LeaderboardEntry> leaderboard = postDAO.getTopUsersByPostCount(5);

        // Create a JSONArray to hold the leaderboard entries
        JSONArray jsonArray = new JSONArray();

        // Convert each LeaderboardEntry to a JSONObject and add it to the JSONArray
        for (LeaderboardEntry entry : leaderboard) {
            JSONObject jsonEntry = new JSONObject();
            jsonEntry.put("username", entry.getUserName());
            jsonEntry.put("userId", entry.getUserId());
            jsonEntry.put("count", entry.getPostOrQuestionCount());
            jsonArray.put(jsonEntry);
        }

        // Set response content type to JSO
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject data = new JSONObject();
        data.put("success", true);
        data.put("data", jsonArray);
        // Write JSON response
        PrintWriter out = response.getWriter();
        out.print(data);
    }
}