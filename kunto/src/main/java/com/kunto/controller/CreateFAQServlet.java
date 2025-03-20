package com.kunto.controller;


import java.io.IOException;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.dao.FAQDao;
@WebServlet("/createFAQ")
public class CreateFAQServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FAQDao faqDAO;

    public void init() {
    	faqDAO=FAQDao.getInstance();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        System.out.println("insert faq.......");

        try {
            // Read request data
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            // Convert JSON to Java object
            JSONObject jsonRequest = new JSONObject(sb.toString());
            String title = jsonRequest.getString("title");
            String questionText = jsonRequest.getString("questionText");
           // int userId = jsonRequest.getInt("userId");
            HttpSession session=request.getSession(false);
            int userId = (int) session.getAttribute("user_id");
            System.out.println(session);
            System.out.println(userId);

            // Validate input
            if (title.isEmpty() || questionText.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Title and question text are required.\"}");
                return;
            }
            Connection connection=DBConnection.getInstance().getConnection();
            // Save FAQ to database
            int faqId = faqDAO.createFAQ(userId, title, questionText,connection);
            if (faqId > 0) {
                out.write("{\"success\": true, \"id\": " + faqId + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"Failed to create question.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Server error.\"}");
        }
    }
}

