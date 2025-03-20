package com.kunto.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

import com.kunto.dao.*;
import com.kunto.util.Mail;
import com.kunto.util.Utils;

@WebServlet("/ku_calorie_burned")  // Matches your fetch request URL
public class CalorieBurned extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON data from request body
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	HttpSession session=request.getSession(false);

    	int user_id=(int) session.getAttribute("user_id");    	
    	CalorieBurnedDAO calorieBurnedDAO=new CalorieBurnedDAO();
    	JSONObject json=new JSONObject();
    	json.put("Workout", calorieBurnedDAO.getCalBurnData(user_id,"workout"));
    	json.put("Activity", calorieBurnedDAO.getCalBurnData(user_id,"activity"));
    	json.put("Walking", calorieBurnedDAO.getCalBurnData(user_id,"walking"));
    	
    	
		/*
		 * Mail mail=Mail.getInstance("zskunto@gmail.com","ysjm tovg bqzo dkzp");
		 * mail.sendOTP("joelrajr26@gmail.com");
		 * 
		 * 
		 */
    	
    	response.getWriter().println(json);
    	
    	
    	
    }


}
