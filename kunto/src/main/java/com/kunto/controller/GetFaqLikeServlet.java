package com.kunto.controller;


// GetFaqLikeServlet.java

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.dao.FAQDao;

@WebServlet("/getFaqLike")
public class GetFaqLikeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int answerId = Integer.parseInt(request.getParameter("postId"));
       // int userId = Integer.parseInt(request.getParameter("userId"));
        HttpSession session=request.getSession(false);
        int userId = (int) session.getAttribute("user_id");
        JSONObject result = FAQDao.getInstance().getFaqLikeHistory(answerId, userId);
		response.setContentType("application/json");
		response.getWriter().write(result.toString());
    }
}

