package com.kunto.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

import com.kunto.dao.*;

@WebServlet("/workout_logs")  // Matches your fetch request URL
public class WorkoutTime extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON data from request body
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	int user_id=1;
    	System.out.println("hi");
    	
    	WorkoutDao workoutDao=new WorkoutDao();
    	JSONObject json=workoutDao.getOneWeekWorkoutData(user_id);
    	
    	response.getWriter().println(json);
    	
    	
    	
    	
    	
    	
    }


}
