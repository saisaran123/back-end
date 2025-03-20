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

public class WorkoutDao{
	
	public  JSONObject getOneWeekWorkoutData(int user_id) {
		ArrayList<LocalDate> prevDays=WeeklyWalkDao.getPrevDays();
		JSONObject json=new JSONObject();
		int l=prevDays.size();
		String q="select workout_hrs from workout_logs where user_id=? and date_logged=?";
		System.out.println(prevDays);
		try {
			Connection connection=DBConnection.getInstance().getConnection();
			for(int i=0;i<l;i++) {
				PreparedStatement ps=connection.prepareStatement(q);
				ps.setInt(1,user_id);
				ps.setDate(2, Date.valueOf(prevDays.get(i)));
				
				ResultSet rs=ps.executeQuery();
				if(rs.next()) {
					json.put(prevDays.get(i).getDayOfWeek().toString(), rs.getDouble("workout_hrs"));
				}

				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
		
	}
}