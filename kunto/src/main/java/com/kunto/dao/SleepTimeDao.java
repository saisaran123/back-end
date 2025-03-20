package com.kunto.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.util.Utils;

public class SleepTimeDao{
	
	public  JSONObject getOneWeekSleepData(int user_id) {
		ArrayList<LocalDate> prevDays=Utils.getPrevDays();
		JSONObject json=new JSONObject();
		System.out.println();
		System.out.println();
		System.out.println("ii");
		System.out.println();
		System.out.println();

		System.out.println(prevDays);
		int l=prevDays.size();
		String q="select sleep_time,wake_time from sleep_tracker where user_id=? and logged_at=?";
		System.out.println(prevDays);
		try {
			Connection connection=DBConnection.getInstance().getConnection();
			for(int i=0;i<l;i++) {
				PreparedStatement ps=connection.prepareStatement(q);
				ps.setInt(1,user_id);
				ps.setDate(2, Date.valueOf(prevDays.get(i)));
				
				ResultSet rs=ps.executeQuery();
				if(rs.next()) {
			        Duration duration = Duration.between(rs.getTime("sleep_time").toLocalTime(), rs.getTime("wake_time").toLocalTime());
			        if (duration.isNegative()) {
			            duration = duration.plusHours(24); // Adjust for overnight cases
			        }
			        double decimalHours = duration.toHours() + (duration.toMinutesPart() / 60.0);

					json.put(prevDays.get(i).getDayOfWeek().toString(),  String.format("%.2f", decimalHours));
					System.out.println(json.toString());
				}

				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
		
	}
}