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
import com.kunto.util.Utils;

public class CalorieBurnedDAO{
	
	
	
	public JSONObject getCalBurnData(int user_id,String name) {
		
		JSONObject json=new JSONObject();
		ArrayList<LocalDate> prevdays=Utils.getPrevDays();
		int l=prevdays.size();
		String q="select calorie_burned from calorie_burned where user_id=? and date_logged=? and activity_name = 'Workout'";
		if(name.equals("activity")) {
			q="select calorie_burned from calorie_burned where user_id=? and date_logged=? and activity_name != 'Workout' and activity_name != 'Walking'";
		}
		else if(name.equals("walking")) {
			q="select km_walked * 50.0 as calorie_burned from walk_log where user_id=? and date_logged=?;";

		}
		System.out.println(prevdays);
		try {
			Connection connection=DBConnection.getInstance().getConnection();
			for(int i=0;i<l;i++) {
				PreparedStatement ps=connection.prepareStatement(q);
				ps.setInt(1,user_id);
				ps.setDate(2, Date.valueOf(prevdays.get(i)));
				System.out.println("hiii"+prevdays.get(i));
				ResultSet rs=ps.executeQuery();
				double temp=0;
				while(rs.next()) {
					temp+=rs.getDouble("calorie_burned");
				}
					json.put(prevdays.get(i).getDayOfWeek().toString(), temp);
					
		
				
				
			}
			System.out.println("workout data");
			System.out.println(json.toString());
			
			return json;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}