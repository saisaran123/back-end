package com.kunto.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Soundbank;

import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.util.Utils;

public class WeeklyWalkDao{


	
	public  JSONObject getWeeklyWalkData(int user_id) {
		JSONObject json=new JSONObject();
		ArrayList<LocalDate> prevdays=Utils.getPrevDays();
		int l=prevdays.size();
		String q="select km_walked from walk_log where user_id=? and date_logged=?";
		System.out.println(prevdays);
		try {
			Connection connection=DBConnection.getInstance().getConnection();
			for(int i=0;i<l;i++) {
				PreparedStatement ps=connection.prepareStatement(q);
				ps.setInt(1,user_id);
				ps.setDate(2, Date.valueOf(prevdays.get(i)));
				
				ResultSet rs=ps.executeQuery();
				if(rs.next()) {
					json.put(prevdays.get(i).getDayOfWeek().toString(), rs.getDouble("km_walked"));
				}
				
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}

	

	
	
}