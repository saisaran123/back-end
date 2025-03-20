package com.kunto.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.kunto.config.DBConnection;
import com.kunto.dao.FAQDao;


@WebServlet("/faqLike")
public class FaqLikeServlet extends HttpServlet {
//  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//      BufferedReader reader = request.getReader();
//      JSONObject json = new JSONObject(new JSONTokener(reader));
//      int answerId = json.getInt("postId");
//      int userId = json.getInt("userId");
//      try {
//    	  FAQDao faqDao=FAQDao.getInstance();
//          boolean status = faqDao.toggleFaqLike(answerId, userId);
//          response.setContentType("application/json");
//          response.getWriter().write(new JSONObject().put("status", status).toString());
//      } catch (SQLException e) {
//          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//      }
//  }
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      BufferedReader reader = request.getReader();
      JSONObject json = new JSONObject(new JSONTokener(reader));
      int answerId = json.getInt("postId");
    //  int userId = json.getInt("userId");
      HttpSession session=request.getSession(false);
      int userId = (int) session.getAttribute("user_id");
      FAQDao faqDao=FAQDao.getInstance();
	  Connection connection = null;
	try {
		connection = DBConnection.getInstance().getConnection();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  boolean status = faqDao.userHasLikedQuestion(answerId, userId,connection);
	  response.setContentType("application/json");
	  response.getWriter().write(new JSONObject().put("status", status).toString());
  }
}
