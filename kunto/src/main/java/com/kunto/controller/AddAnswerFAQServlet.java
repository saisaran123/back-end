package com.kunto.controller;


import java.io.*;
import java.io.BufferedReader;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

import com.kunto.dao.FAQDao;
@WebServlet("/answerFAQ")
public class AddAnswerFAQServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        JSONObject json = new JSONObject(sb.toString());

        int questionId = json.getInt("id");
       // int userId = json.getInt("userId");
        HttpSession session=request.getSession(false);
        int userId = (int) session.getAttribute("user_id");
        System.out.println(session);
        System.out.println(userId);

        String answerText = json.getString("reply");

        FAQDao dao = FAQDao.getInstance();
		boolean success = dao.addFaqAnswer(questionId, userId, answerText);
		response.setContentType("application/json");
		response.getWriter().write(new JSONObject().put("status", success).toString());
    }
}
