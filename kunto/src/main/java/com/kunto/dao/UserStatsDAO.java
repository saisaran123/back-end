package com.kunto.dao;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.util.Utils;

public class UserStatsDAO {
    
    private Connection conn;

    public UserStatsDAO() throws SQLException {
        this.conn = DBConnection.getInstance().getConnection();
    }

    public JSONObject getUserGoals(int userId) throws SQLException {
        String query = "SELECT goal_calories, goal_protein, goal_carbs, goal_fat, goal_fiber, goal_water,goal_kilometer_walked FROM user_goals WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            JSONObject goalData = new JSONObject();
            if (rs.next()) {
                goalData.put("goal_calories", rs.getDouble("goal_calories"));
                goalData.put("goal_protein", rs.getDouble("goal_protein"));
                goalData.put("goal_carbs", rs.getDouble("goal_carbs"));
                goalData.put("goal_fat", rs.getDouble("goal_fat"));
                goalData.put("goal_fiber", rs.getDouble("goal_fiber"));
                goalData.put("goal_water", rs.getDouble("goal_water"));
                goalData.put("goal_kilometer_walked", rs.getDouble("goal_kilometer_walked"));

                
            }
            return goalData;
        }
    }

    public JSONObject getDailyIntake(int userId, String currentDate) throws SQLException {
        String query = "SELECT calories, protein, carbs, fat, fiber FROM daily_nutrition WHERE user_id = ? AND log_date = ?";
        String q="select km_walked from walk_log where user_id=? and date_logged=?";
        try (PreparedStatement stmt = conn.prepareStatement(query);
        		PreparedStatement ps=conn.prepareStatement(q)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(currentDate));
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(currentDate));
            ResultSet rs = stmt.executeQuery();
            ResultSet rs2=ps.executeQuery();
            

            JSONObject dailyData = new JSONObject();
            if(rs2.next()) {
            	dailyData.put("km_walked",rs2.getDouble("km_walked"));
            }
            else {
            	dailyData.put("km_walked",0);


            }
            if (rs.next()) {
                dailyData.put("total_calories", rs.getDouble("calories"));
                dailyData.put("total_protein", rs.getDouble("protein"));
                dailyData.put("total_carbs", rs.getDouble("carbs"));
                dailyData.put("total_fat", rs.getDouble("fat"));
                dailyData.put("total_fiber", rs.getDouble("fiber"));
            } else {
                dailyData.put("total_calories", 0);
                dailyData.put("total_protein", 0);
                dailyData.put("total_carbs", 0);
                dailyData.put("total_fat", 0);
                dailyData.put("total_fiber", 0);
            }
            return dailyData;
        }
    }

    public double getWaterIntake(int userId, String currentDate) throws SQLException {
        String queryIncrement = "SELECT SUM(water_drank) AS total_water FROM user_water_log WHERE user_id = ? AND DATE(consumed_at) = ? AND change_type = 'increment'";
        String queryDecrement = "SELECT SUM(water_drank) AS total_water FROM user_water_log WHERE user_id = ? AND DATE(consumed_at) = ? AND change_type = 'decrement'";
        
        double totalWater = 0;

        try (PreparedStatement stmtInc = conn.prepareStatement(queryIncrement);
             PreparedStatement stmtDec = conn.prepareStatement(queryDecrement)) {

            stmtInc.setInt(1, userId);
            stmtInc.setDate(2, Date.valueOf(currentDate));
            ResultSet rsInc = stmtInc.executeQuery();
            
            stmtDec.setInt(1, userId);
            stmtDec.setDate(2, Date.valueOf(currentDate));
            ResultSet rsDec = stmtDec.executeQuery();

            if (rsInc.next()) totalWater += rsInc.getDouble("total_water");
            if (rsDec.next()) totalWater -= rsDec.getDouble("total_water");
        }
        return totalWater;
    }

    public JSONObject getMealTimeData(int userId, String currentDate) throws SQLException {
        String query = "SELECT meal_type, SUM(calories / 100 * quantity) AS total_calories FROM user_meal_log WHERE user_id = ? AND DATE(logged_at) = ? GROUP BY meal_type";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(currentDate));
            ResultSet rs = stmt.executeQuery();

            JSONObject mealData = new JSONObject();
            mealData.put("breakfast", 0);
            mealData.put("snacks", 0);
            mealData.put("lunch", 0);
            mealData.put("dinner", 0);

            while (rs.next()) {
                String mealType = rs.getString("meal_type").toLowerCase();
                mealData.put(mealType, rs.getDouble("total_calories"));
            }
            return mealData;
        }
    }

    public JSONArray getFoodLog(int userId, String currentDate) throws SQLException {
        String query = "SELECT food_name, quantity, logged_at FROM user_meal_log WHERE user_id = ? AND DATE(logged_at) = ?";
        String waterQuery = "SELECT consumed_at FROM user_water_log WHERE user_id = ? AND DATE(consumed_at) = ? AND change_type = 'increment'";

        JSONArray foodLogArray = new JSONArray();

        try (PreparedStatement stmt = conn.prepareStatement(query);
             PreparedStatement waterStmt = conn.prepareStatement(waterQuery)) {

            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(currentDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                JSONObject foodLog = new JSONObject();
                foodLog.put("food_name", rs.getString("food_name"));
                foodLog.put("time_logged", rs.getTimestamp("logged_at").toString().split(" ")[1]);
                foodLog.put("quantity", rs.getInt("quantity"));
                foodLogArray.put(foodLog);
            }

            waterStmt.setInt(1, userId);
            waterStmt.setDate(2, Date.valueOf(currentDate));
            ResultSet waterRs = waterStmt.executeQuery();

            while (waterRs.next()) {
                JSONObject foodLog = new JSONObject();
                foodLog.put("food_name", "water");
                foodLog.put("time_logged", waterRs.getTimestamp("consumed_at").toString().split(" ")[1]);
                foodLog.put("quantity", 250);
                foodLogArray.put(foodLog);
            }
        }
        return foodLogArray;
    }
    
    public JSONObject calorieIntake(int user_id) {
    	JSONObject json=new JSONObject();
    	ArrayList<LocalDate> prevDays=Utils.getPrevDays();
    	
		int l=prevDays.size();
		String q="select calories from daily_nutrition where user_id=? and log_date=?";
		System.out.println("errrro");
		System.out.println(prevDays);
		try {
			Connection connection=DBConnection.getInstance().getConnection();
			for(int i=0;i<l;i++) {
				PreparedStatement ps=connection.prepareStatement(q);
				ps.setInt(1,user_id);
				ps.setDate(2, Date.valueOf(prevDays.get(i)));
				
				ResultSet rs=ps.executeQuery();
				if(rs.next()) {
					json.put(prevDays.get(i).getDayOfWeek().toString(), rs.getInt("calories"));
				}
				
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
	}

	public String getSleepTime(int userId, String currentDate) {
		// TODO Auto-generated method stub
		String q="select sleep_time,wake_time from sleep_tracker where user_id=? and logged_at=?";
		try {
			PreparedStatement ps=DBConnection.getInstance().getConnection().prepareStatement(q);
			ps.setInt(1, userId);
			System.out.println("before");
			ps.setDate(2, Date.valueOf(currentDate));
			System.out.println("after");

			ResultSet rs=ps.executeQuery();
			
			if (rs.next()) {
			    Duration duration = Duration.between(rs.getTime("sleep_time").toLocalTime(), rs.getTime("wake_time").toLocalTime());
			    if (duration.isNegative()) {
			        duration = duration.plusHours(24); // Adjust for overnight cases
			    }

			    long hours = duration.toHours();
			    long minutes = duration.toMinutesPart();
			    
			    return String.format("%d.%02d", hours, minutes); // Format as HH.MM
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}
	
	

	public static String sumWorkoutTime(List<String> workoutTimes) {
		System.out.println(workoutTimes);
        Duration totalDuration = Duration.ZERO;

        for (String time : workoutTimes) {
	            LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
            totalDuration = totalDuration.plus(Duration.between(LocalTime.MIN, localTime));
        }
System.out.println("TOTAL DURATIONNNNNNNNNNNNNNNNNN"+totalDuration);
        long hours = totalDuration.toHours();
        long minutes = totalDuration.toMinutesPart();
        long seconds = totalDuration.toSecondsPart();

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Method to get total workout time for a user on a specific date
	public String getTotalTime(int userId, String currentDate) {
	    String query = "SELECT workout_time FROM calorie_burned WHERE user_id = ? AND date_logged = ?";
	    List<String> workoutTimes = new ArrayList<>();

	    try {
	        Connection connection = DBConnection.getInstance().getConnection();
	        PreparedStatement ps = connection.prepareStatement(query);

	        ps.setInt(1, userId);
	        ps.setDate(2, Date.valueOf(currentDate)); // Convert String to SQL Date

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            workoutTimes.add(rs.getString("workout_time"));
	        }

	        if (workoutTimes.isEmpty()) {
	            return "00:00:00"; // No workouts found
	        }

	        return sumWorkoutTime(workoutTimes);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "00:00:00"; // Return default if an error occurs
	}

}
