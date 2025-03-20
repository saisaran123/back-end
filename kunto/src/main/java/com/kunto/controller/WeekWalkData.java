package com.kunto.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

import com.kunto.dao.*;

@WebServlet("/weekly_walk")  // Matches your fetch request URL
public class WeekWalkData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON data from request body
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	HttpSession session=request.getSession(false);

    	int user_id=(int) session.getAttribute("user_id");
    	WeeklyWalkDao weakwalkdao=new WeeklyWalkDao();
    	JSONObject json=weakwalkdao.getWeeklyWalkData(user_id);
    	
    	response.getWriter().println(json);
    	
    	
    	
    	
    	
    	
    }


}
