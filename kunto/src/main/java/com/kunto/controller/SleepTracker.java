package com.kunto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.dao.SleepDao;
import com.kunto.models.Sleep;
import com.kunto.util.Utils;

@WebServlet("/sleepTrack")  // Matches your fetch request URL
public class SleepTracker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON data from request body
    	response.setContentType("application/json");
    	System.out.println("called");

    	JSONObject json=Utils.getJsonObject(request);
    	
    	LocalTime sleepTime=LocalTime.parse(json.getString("sleep_time"));
    	LocalTime wakeUpTime=LocalTime.parse(json.getString("wake_up_time"));
    	LocalDate today=LocalDate.parse(json.getString("date"));
    	HttpSession session=request.getSession(false);

    	int userId=(int) session.getAttribute("user_id");
    	
    	
    	Sleep sleep=new Sleep(userId,sleepTime,wakeUpTime,today);
    	
    	SleepDao.getInstance().setConnection();
    	
    	boolean isInserted=SleepDao.getInstance().insertSleepData(sleep);
    	JSONObject out=new JSONObject();
    	if(isInserted) {
    		out.put("message", "success");
    	}
    	else {
    		out.put("message", "failure");

    	}
    	response.getWriter().println(out);
    	
    	
    }
}
