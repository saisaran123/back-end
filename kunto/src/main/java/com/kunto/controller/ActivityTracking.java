package com.kunto.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

import com.kunto.dao.*;

@WebServlet("/activity_stat")  // Matches your fetch request URL
public class ActivityTracking extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON data from request body
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	HttpSession session=request.getSession(false);

    	int user_id=(int) session.getAttribute("user_id");    	System.out.println("hikjj");
    	
    	ActivityTrackingDao activityTrackingDao=new ActivityTrackingDao();
    	JSONObject json=activityTrackingDao.getOneMonthCalorieData(user_id);
    	
    	response.getWriter().println(json);
    	
    	
    	
    	
    	
    	
    }


}
