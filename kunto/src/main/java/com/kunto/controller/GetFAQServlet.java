package com.kunto.controller;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.FAQDao;
import com.kunto.models.FAQ;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getFAQ")
public class GetFAQServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	System.out.println("get faqss");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        int from = Integer.parseInt(request.getParameter("from"));
        int to = Integer.parseInt(request.getParameter("to"));
        System.out.println("getFAQ" + from + " " + to);

        FAQDao faqDAO=null;
		faqDAO = FAQDao.getInstance();
        List<FAQ> faqs = faqDAO.getFAQs(from, to);  // Get FAQs from DAO

        JSONArray faqArray = new JSONArray();

        for (FAQ faq : faqs) {
            JSONObject faqJson = new JSONObject();
            faqJson.put("id", faq.getId());
            faqJson.put("userId", faq.getUserId());
            faqJson.put("username", faq.getUsername());
            faqJson.put("title", faq.getTitle());  // Include title
            faqJson.put("questionText", faq.getQuestionText());
            faqJson.put("createdAt", faq.getCreatedAt().toString());
            faqJson.put("view", faq.getViews());

            faqArray.put(faqJson);
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("status", "success");
        responseJson.put("faqs", faqArray);

        response.getWriter().write(responseJson.toString());
    }
}
