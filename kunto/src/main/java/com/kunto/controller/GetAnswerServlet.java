//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.kunto.dao.FAQDao;
//import com.kunto.model.FAQAnswer;
//
//@WebServlet("/getAnswers")
//public class GetAnswerServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int faqId = Integer.parseInt(request.getParameter("faqId"));
//        System.out.println(faqId);
//        
//        // Get answers from the DAO
//        FAQDao faqDAO =null;
//		try {
//			faqDAO = FAQDao.getInstance();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        List<FAQAnswer> answers = faqDAO.getAnswersForFAQ(faqId);
//        
//        // Set the response content type to JSON
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        JSONObject jsonObject=new JSONObject();
//        // Manually build the JSON response
//        StringBuilder jsonResponse = new StringBuilder();
//        jsonResponse.append("[");
//
//        for (int i = 0; i < answers.size(); i++) {
//            FAQAnswer answer = answers.get(i);
//            jsonResponse.append("{");
//            jsonResponse.append("\"answerId\":").append(answer.getAnswerId()).append(",");
//            jsonResponse.append("\"faqId\":").append(answer.getFaqId()).append(",");
//            jsonResponse.append("\"userId\":").append(answer.getUserId()).append(",");
//            jsonResponse.append("\"answer\":\"").append(answer.getAnswer().replace("\"", "\\\"")).append("\",");
//            jsonResponse.append("\"createdAt\":\"").append(answer.getCreatedAt()).append("\"");
//            jsonResponse.append("\"userLiked\":\"").append(answer.isUserLIked()).append("\"");
//            jsonResponse.append("\"totalLikes\":\"").append(answer.getLikeCount()).append("\"");
//            jsonResponse.append("}");
//            
//            if (i < answers.size() - 1) {
//                jsonResponse.append(",");
//            }
//        }
//        
//        jsonResponse.append("]");
//        jsonObject.put("answers", jsonResponse.toString());
//        
//        // Send the JSON response back to the client
//        response.getWriter().println(jsonObject);
//    }
//}




package com.kunto.controller;






import java.io.IOException;
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
import com.kunto.models.FAQAnswer;

@WebServlet("/getAnswers")
public class GetAnswerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int faqId = Integer.parseInt(request.getParameter("faqId"));
        System.out.println(faqId);
        
        FAQDao faqDAO = null;
        faqDAO = FAQDao.getInstance();

        List<FAQAnswer> answers = faqDAO.getAnswersForFAQ(faqId);

        // Create JSON response
        JSONArray answersArray = new JSONArray();
        for (FAQAnswer answer : answers) {
            JSONObject answerJson = new JSONObject();
            answerJson.put("answerId", answer.getAnswerId());
            answerJson.put("faqId", answer.getFaqId());
            answerJson.put("userId", answer.getUserId());
            answerJson.put("username", answer.getUsername());
            answerJson.put("answer", answer.getAnswer());
            answerJson.put("createdAt", answer.getCreatedAt());
            answerJson.put("userLiked", answer.isUserLIked());
            answerJson.put("totalLikes", answer.getLikeCount());

            answersArray.put(answerJson);
        }

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("answers", answersArray);

        // Set response headers and send response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(jsonResponse);
    }
}
