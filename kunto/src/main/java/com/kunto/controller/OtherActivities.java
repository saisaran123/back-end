package com.kunto.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

import com.kunto.dao.*;
import com.kunto.util.*;

@WebServlet("/other_activities")  // Matches your fetch request URL
public class OtherActivities extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON data from request body
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	HttpSession session=request.getSession(false);

    	int user_id=(int) session.getAttribute("user_id");
    	
    	JSONObject reqJson=Utils.getJsonObject(request);
    	System.out.println("other activity called");
    	OtherActivitiesDAO otherActivitiesDAO=new OtherActivitiesDAO();
    	boolean isInserted=otherActivitiesDAO.insertActivity(user_id,reqJson);
    	JSONObject j=new JSONObject();

    	response.getWriter().println(j.put("isInserted", isInserted));
    	
    	
    	
    	
    }


}
