package com.kunto.controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.mysql.cj.x.protobuf.MysqlxCrud.Insert;


public class NutritionServlet {

    
    protected static void getGoal(int user_id ,int age,String gender,double weight,double height,String activityLevel,String goal) {

    	try {
	
		
            double bmr = 0;
            if ("male".equalsIgnoreCase(gender)) {
                bmr = 10 * weight + 6.25 * height - 5 * age + 5;
            } else if ("female".equalsIgnoreCase(gender)) {
                bmr = 10 * weight + 6.25 * height - 5 * age - 161;
            }


            double activityMultiplier;
            switch (activityLevel.toLowerCase()) {
                case "sedentary":
                    activityMultiplier = 1.2;
                    break;
                case "lightly_active":
                    activityMultiplier = 1.375;
                    break;
                case "moderately_active":
                    activityMultiplier = 1.55;
                    break;
                case "very_active":
                    activityMultiplier = 1.725;
                    break;
                case "extra_active":
                    activityMultiplier = 1.9;
                    break;
                default:
                    
                    return;
            }

            double tdee = bmr * activityMultiplier;


            if ("weight_loss".equalsIgnoreCase(goal)) {
                tdee -= 500;
            } else if ("weight_gain".equalsIgnoreCase(goal)) {
                tdee += 500;
            }

            double protein = weight * (goal.equalsIgnoreCase("weight_loss") ? 2.0 : 1.6);
            double proteinCalories = protein * 4;

            double fatPercentage = goal.equalsIgnoreCase("weight_loss") ? 0.25 : 0.3;
            double fat = (tdee * fatPercentage) / 9;
            double fatCalories = fat * 9;

            double carbsCalories = tdee - (proteinCalories + fatCalories);
            double carbs = carbsCalories / 4;


            double fiber = (tdee / 1000) * 14;


            double water = weight * 0.035; 


            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("calories", Math.round(tdee));
            jsonResponse.put("protein", Math.round(protein));
            jsonResponse.put("fats", Math.round(fat));
            jsonResponse.put("carbohydrates", Math.round(carbs));
            jsonResponse.put("fiber", Math.round(fiber));
            jsonResponse.put("water", Math.round(water * 1000));
            
            Connection connection = DBConnection.getInstance().getConnection();
            String queryString = "insert into user_goals(user_id,goal_calories,goal_protein,goal_carbs,goal_fat,goal_fiber,goal_water,goal_kilometer_walked) values(?,?,?,?,?,?,?,?)";
            
            PreparedStatement stm = connection.prepareStatement(queryString);
            stm.setInt(1, user_id);
            stm.setInt(2, (int) Math.round(tdee));
            stm.setInt(3, (int)Math.round(protein));
            stm.setInt(4,  (int)Math.round(carbs));
            stm.setInt(5, (int)Math.round(fat));
            stm.setInt(6, (int)Math.round(fiber));
            stm.setInt(7,  (int) (Math.round(water * 1000)));
            stm.setInt(8, 8);
            
            stm.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
