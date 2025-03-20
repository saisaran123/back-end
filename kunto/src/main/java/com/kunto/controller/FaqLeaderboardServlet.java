package com.kunto.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.FAQDao;
import com.kunto.dao.PostDAO;
import com.kunto.models.LeaderboardEntry;

@WebServlet("/faqCountLeaderBoard")
public class FaqLeaderboardServlet extends HttpServlet {
    

   

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("faqCountLeaderBoard");
    	FAQDao faqDao=FAQDao.getInstance();
        // Fetch leaderboard data
        List<LeaderboardEntry> leaderboard = faqDao.getTopUsersByFAQCount(5);

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