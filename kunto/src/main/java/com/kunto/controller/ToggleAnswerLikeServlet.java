package com.kunto.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.dao.FAQDao;

@WebServlet("/toggleAnswerLike")
public class ToggleAnswerLikeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String requestData = sb.toString();
        JSONObject jsonObject =new JSONObject(requestData);
        System.out.println(jsonObject);
    	
       // int userId = jsonObject.getInt("userId");
        HttpSession session=request.getSession(false);
        int userId = (int) session.getAttribute("user_id");
        int answerId = jsonObject.getInt("answerId");

        JSONObject result = new JSONObject();
        Connection conn = null;
		try {
			conn = DBConnection.getInstance().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FAQDao faqDAO =FAQDao.getInstance();
		boolean liked = faqDAO.toggleFaqAnswerLike(answerId, userId,conn);
		int totalLikes = faqDAO.getFaqAnswerLikeCount(answerId,conn);

		result.put("success", true);
		result.put("liked", liked);
		result.put("totalLikes", totalLikes);

        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }
}
