package com.kunto.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.dao.FAQDao;

@WebServlet("/viewFAQ")
public class FAQViewServlet extends HttpServlet {

    private FAQDao faqDAO;

    @Override
    public void init() {
        faqDAO = FAQDao.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	 StringBuilder sb = new StringBuilder();
    	 String line;
         BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
         while ((line = reader.readLine()) != null) {
             sb.append(line);
         }

         String requestData = sb.toString();
         
             JSONObject json = new JSONObject(requestData);
             System.out.println(requestData);
             int faqId = json.getInt("faq_id");
            // int userId = json.getInt("user_id");
             HttpSession session=request.getSession(false);
             int userId = (int) session.getAttribute("user_id");
             PrintWriter out=response.getWriter();

        try {
            boolean hasViewed = faqDAO.hasUserViewedFAQ(faqId, userId);

            if (!hasViewed) {
                faqDAO.incrementFAQViewCount(faqId, userId);
            }

            int viewCount = faqDAO.getFAQViewCount(faqId);

            // Respond with updated view count
            String jsonResponse = String.format("{\"viewCount\": %d, \"viewed\": %b}", viewCount, hasViewed);
            out.print(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"An error occurred while updating views.\"}");
        } finally {
            out.flush();
        }
    }
}