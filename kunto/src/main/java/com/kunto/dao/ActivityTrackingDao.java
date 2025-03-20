package com.kunto.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONObject;

import com.kunto.config.DBConnection;

public class ActivityTrackingDao{
	
	public  JSONObject getOneMonthCalorieData(int user_id) {
		JSONObject json=new JSONObject();
		LocalDate todayDate=LocalDate.now();
		int l=todayDate.getDayOfMonth();
			
		String q="select calorie_burned from calorie_burned where user_id=? and date_logged=?";
		try {
			Connection connection=DBConnection.getInstance().getConnection();
			for(int i=0;i<=l;i++) {
				PreparedStatement ps=connection.prepareStatement(q);
				ps.setInt(1,user_id);
				ps.setDate(2, Date.valueOf(todayDate.minusDays(i)));
				
				ResultSet rs=ps.executeQuery();
				double temp=0;
				while(rs.next()) {
					temp+=rs.getDouble("calorie_burned");
				}
					json.put(todayDate.minusDays(i).toString(),temp );
				

				
			}
			
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println(json.toString());

			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
		
	}
	
	public JSONObject getOneMonthSleepData(int user_id) {
		JSONObject json=new JSONObject();
		LocalDate todayDate=LocalDate.now();
		int l=todayDate.getDayOfMonth();
			
		String q="select workout_hrs from workout_logs where user_id=? and date_logged=?";
		try {
			Connection connection=DBConnection.getInstance().getConnection();
			for(int i=1;i<=l;i++) {
				PreparedStatement ps=connection.prepareStatement(q);
				ps.setInt(1,user_id);
				ps.setDate(2, Date.valueOf(todayDate.minusDays(i)));
				
				ResultSet rs=ps.executeQuery();
				if(rs.next()) {
					json.put(todayDate.minusDays(i).toString(), rs.getDouble("workout_hrs"));
				}
				else {
					System.out.println(json.toString());
					break;
				}
				
			}
			PreparedStatement ps=connection.prepareStatement(q);
			ps.setInt(1,user_id);
			ps.setDate(2, Date.valueOf(todayDate));
			
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				json.put(todayDate.toString(), rs.getDouble("workout_hrs"));
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
		
	}

}