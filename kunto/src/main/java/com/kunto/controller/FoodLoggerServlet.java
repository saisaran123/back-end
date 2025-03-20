package com.kunto.controller;



import java.io.*;
import java.nio.channels.Pipe.SourceChannel;
import java.sql.*;
//import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

import com.kunto.config.DBConnection;
import com.kunto.util.Utils;



public class FoodLoggerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

        PrintWriter out = response.getWriter();
//        System.out.println("heyyy");

        try {
 
            JSONObject jsonRequest =Utils.getJsonObject(request);
        	HttpSession session=request.getSession(false);

        	int userId=(int) session.getAttribute("user_id");
        	JSONArray foodsArray = jsonRequest.getJSONArray("foods");
            String mealType = jsonRequest.getString("meal_type");
            String currentDate = java.time.LocalDate.now().toString(); 

      
            Connection conn = DBConnection.getInstance().getConnection();

     
            String checkQuery = "SELECT * FROM daily_nutrition WHERE user_id = ? AND log_date = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, userId);
            checkStmt.setDate(2, Date.valueOf(currentDate));
            ResultSet rs = checkStmt.executeQuery();
            
            System.out.println("debug");
            boolean entryExists = rs.next();
            System.out.println("checked");

            double totalCalories = entryExists ? rs.getDouble("calories") : 0;
            double totalProtein = entryExists ? rs.getDouble("protein") : 0;
            double totalFiber = entryExists ? rs.getDouble("fiber") : 0;
            double totalCarbs = entryExists ? rs.getDouble("carbs") : 0;
            double totalFat = entryExists ? rs.getDouble("fat") : 0;
            String insertQuery = "INSERT INTO user_meal_log (user_id, meal_type, food_name, calories, protein, carbs, fat, fiber , quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ? ,?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            for (int i = 0; i < foodsArray.length(); i++) {
                JSONObject food = foodsArray.getJSONObject(i);
                String foodName = food.getString("name");
                double calories = food.getDouble("calories");
                double protein = food.getDouble("protein");
                double fiber = food.getDouble("fiber");
                double carbs = food.getDouble("carbs");
                double fat = food.getDouble("fat");
                double grams = food.getDouble("grams");
                System.out.println("inserting...");

                totalCalories += (calories * grams / 100);
                totalProtein += (protein * grams / 100);
                totalFiber += (fiber * grams / 100);
                totalCarbs += (carbs * grams / 100);
                totalFat += (fat * grams / 100);
                
                
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, mealType.toLowerCase());
                insertStmt.setString(3, foodName);
                insertStmt.setDouble(4, calories);
                insertStmt.setDouble(5, protein);
                insertStmt.setDouble(6, carbs);
                insertStmt.setDouble(7, fat);
                insertStmt.setDouble(8, fiber);
                insertStmt.setDouble(9, grams);
                insertStmt.executeUpdate();
          
            }
           

            if (entryExists) {
            	 System.out.println("hello");
                String updateQuery = "UPDATE daily_nutrition SET calories = ?, protein = ?, fiber = ?, carbs = ?, fat = ? WHERE user_id = ? AND log_date = ?";
                
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setDouble(1, totalCalories);
                updateStmt.setDouble(2, totalProtein);
                updateStmt.setDouble(3, totalFiber);
                updateStmt.setDouble(4, totalCarbs);
                updateStmt.setDouble(5, totalFat);
                updateStmt.setInt(6, userId);
                updateStmt.setDate(7, Date.valueOf(currentDate));
                updateStmt.executeUpdate();
                updateStmt.close();
                System.out.println("bye");
            } else {
            	System.out.println("");
                String insertDailyQuery = "INSERT INTO daily_nutrition (user_id, log_date, calories, protein, fiber, carbs, fat) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertDailyStmt = conn.prepareStatement(insertDailyQuery);
                insertDailyStmt.setInt(1, userId);
                insertDailyStmt.setDate(2, Date.valueOf(currentDate));
                insertDailyStmt.setDouble(3, totalCalories);
                insertDailyStmt.setDouble(4, totalProtein);
                insertDailyStmt.setDouble(5, totalFiber);
                insertDailyStmt.setDouble(6, totalCarbs);
                insertDailyStmt.setDouble(7, totalFat);
                insertDailyStmt.executeUpdate();
                insertDailyStmt.close();
            }

            
            insertStmt.close();	

            checkStmt.close();


            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "successfull");
            out.print(jsonResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Failed to log food data: " + e.getMessage());
            out.print(errorResponse.toString());
        }
    }
}


