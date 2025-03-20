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

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.dao.FAQDao;

@WebServlet("/likeQuestion")
public class LikeQuestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String requestData = sb.toString();
        JSONObject jsonObject =new JSONObject(requestData);
        System.out.println(jsonObject);
    	
      //  int userId = jsonObject.getInt("userId");
        HttpSession session=request.getSession(false);
        int userId = (int) session.getAttribute("user_id");
        int questionId  = jsonObject.getInt("postId");

       

        Connection conn = null;
		try {
			conn = DBConnection.getInstance().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FAQDao faqDAO = FAQDao.getInstance();
		boolean isLiked = faqDAO.toggleQuestionLike(userId, questionId,conn);
		int totalLikes = faqDAO.getQuestionLikeCount(questionId,conn);

		// Prepare JSON response
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("liked", isLiked);
		jsonResponse.put("totalLikes", totalLikes);
		out.println(jsonResponse.toString());
    }
}
