package com.kunto.dao;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import com.kunto.config.DBConnection;

public class ManualWorkoutDao{

	public JSONObject searchVideos(String toSearch) {
		// TODO Auto-generated method stub
		String query="SELECT  wv.name FROM workout_videos wv JOIN workout_type wt ON wv.workout_type_id = wt.id WHERE wt.name =?";
		JSONObject json=new JSONObject();
		Connection connection=null;
		int i=0;
		try {
			connection=DBConnection.getInstance().getConnection();
			PreparedStatement ps=connection.prepareStatement(query);
			System.out.println("no problem");
			ps.setString(1,toSearch);
			System.out.println(query);

			
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				String workout=rs.getString("name");
				
				json.put(workout.substring(42,workout.length() - 4),workout);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	
	
}