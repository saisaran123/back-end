package com.kunto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.dao.SleepDao;
import com.kunto.dao.WalkDao;
import com.kunto.models.Sleep;
import com.kunto.models.WalkHistory;
import com.kunto.models.WalkLog;

@WebServlet("/log_kilometers")  // Matches your fetch request URL
public class KilometerTrack extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON data from request body
    	
    	double km=Double.parseDouble(request.getParameter("kilometer"));
    	LocalDate date=LocalDate.now();
    	LocalDateTime dateTime=LocalDateTime.now();
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	HttpSession session=request.getSession(false);

    	int user_id=(int) session.getAttribute("user_id");    	
    	WalkLog walkLog=new WalkLog(user_id,date,km);
    	WalkHistory walkHistory=new WalkHistory(user_id,km,dateTime);
    	
    	boolean isInserted=WalkDao.logWalk(walkLog);
    	boolean isHistoryAdded=WalkDao.addWalkHistory(walkHistory);
    	
    	if(isInserted&&isHistoryAdded) {
    		JSONObject json=WalkDao.getWalkDetails(walkLog);
    		
    		System.out.println(json);
    		response.getWriter().println(json);
    	}else {
    		JSONObject json=new JSONObject();
    		json.put("status", false);
    		System.out.println(json);
    		response.getWriter().println(json);

    	}
		/*
		 * JSONObject json=new JSONObject(); json.put("status", "error");
		 * response.getWriter().println(json);
		 */
    	
    	
    	
    }


}
