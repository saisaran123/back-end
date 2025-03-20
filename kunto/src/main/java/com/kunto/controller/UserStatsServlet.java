package com.kunto.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;
import org.json.JSONArray;

import com.kunto.dao.UserStatsDAO;

@WebServlet("/getUserStats")
public class UserStatsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
        	HttpSession session=request.getSession(false);

        	int userId=(int) session.getAttribute("user_id");
            String currentDate = request.getParameter("date");

            UserStatsDAO dao = new UserStatsDAO();

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("goal", dao.getUserGoals(userId));


            jsonResponse.put("sleep_time", dao.getSleepTime(userId,currentDate));

            
            System.out.println("not");
            System.out.println("ont");
            System.out.println("tno");
            JSONObject dailyData = dao.getDailyIntake(userId, currentDate);
            dailyData.put("total_water", dao.getWaterIntake(userId, currentDate));
            jsonResponse.put("daily_intake", dailyData);

            System.out.println(dailyData.toString());
            jsonResponse.put("meal_time", dao.getMealTimeData(userId, currentDate));
            jsonResponse.put("log", dao.getFoodLog(userId, currentDate));
			
			  jsonResponse.put("total_time_ac", dao.getTotalTime(userId,currentDate));
			         jsonResponse.put("calorie_intake_data", dao.calorieIntake(userId));
            out.print(jsonResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print(new JSONObject().put("error", "Failed to retrieve data: " + e.getMessage()).toString());
        }
    }
}
