package com.kunto.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.models.WalkHistory;
import com.kunto.models.WalkLog;

public class WalkDao {

public static boolean logWalk(WalkLog walkLog) {
    String insertQuery = "INSERT INTO walk_log (user_id, date_logged, km_walked) VALUES (?, ?, ?)";
    String checkQuery = "SELECT km_walked FROM walk_log WHERE user_id = ? AND date_logged = ?";
    String updateQuery = "UPDATE walk_log SET km_walked = km_walked + ? WHERE user_id = ? AND date_logged = ?";

    Connection connection = null;
    PreparedStatement checkPs = null;
    PreparedStatement insertPs = null;
    PreparedStatement updatePs = null;
    ResultSet rs = null;

    try {
        connection = DBConnection.getInstance().getConnection();
        checkPs = connection.prepareStatement(checkQuery);
        insertPs = connection.prepareStatement(insertQuery);
        updatePs = connection.prepareStatement(updateQuery);

        int userId = walkLog.getUserId();
        Date dateLogged = Date.valueOf(walkLog.getDateLogged());
        double kmWalked = walkLog.getKmWalked();

        System.out.println("Checking walk log for user: " + userId + " on " + dateLogged);

        // Check if the walk entry already exists for today
        checkPs.setInt(1, userId);
        checkPs.setDate(2, dateLogged);
        rs = checkPs.executeQuery();

        if (rs.next()) {
            double existingKm = rs.getDouble("km_walked");
            System.out.println("Existing record found. Adding new km to existing value...");
            System.out.println("km-log"+kmWalked);
            updatePs.setDouble(1, Math.round(kmWalked * 100.0) / 100.0);  // Add new km to existing km
            updatePs.setInt(2, userId);
            updatePs.setDate(3, dateLogged);

            if (updatePs.executeUpdate() >= 1) {
                return true;
            }
        } else {
            System.out.println("No existing record found. Inserting new...");
            insertPs.setInt(1, userId);
            insertPs.setDate(2, dateLogged);
            insertPs.setDouble(3, Math.round(kmWalked * 100.0) / 100.0);

            if (insertPs.executeUpdate() >= 1) {
                return true;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } 
    return false;
}

	public static boolean addWalkHistory(WalkHistory walkHistory) {
		String insert="insert into walk_history (user_id,km_walked) values(?,?)";
		try {
			Connection connection=DBConnection.getInstance().getConnection();
			PreparedStatement ps=connection.prepareStatement(insert);
			
			ps.setInt(1, walkHistory.getUserId());
			ps.setDouble(2, walkHistory.getKmWalked());

			return ps.executeUpdate()>=1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static JSONObject getWalkDetails(WalkLog walkLog) {
		// TODO Auto-generated method stub
		double total_walk=-1;
		double goal=-1;
		JSONObject json=new JSONObject();
		Connection connection;
		try {
			connection = DBConnection.getInstance().getConnection();
			String q="select km_walked from walk_log where user_id =? and date_logged=?";
			String q2="select goal_kilometer_walked from user_goals where user_id=?";
			
			PreparedStatement ps=connection.prepareStatement(q);
			PreparedStatement ps2=connection.prepareStatement(q2);
			
			ps.setInt(1,walkLog.getUserId());
			ps.setDate(2,Date.valueOf(walkLog.getDateLogged()));
			ps2.setInt(1, walkLog.getUserId());
			
			ResultSet rs=ps.executeQuery();
			ResultSet rs1=ps2.executeQuery();
			if(rs.next()) {
				total_walk=rs.getDouble("km_walked");
			}
			if(rs1.next()) {
				goal=rs1.getDouble("goal_kilometer_walked");
			}
			
			if(total_walk!=-1 && goal!=-1) {
				json.put("total_walk", total_walk);
				json.put("goal_walk", goal);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return json;
	}
	
	public static boolean putCalorieBurn(WalkLog walkLog) {
	    String checkExistence = "SELECT calorie_burned FROM calorie_burned WHERE user_id = ? AND date_logged = ?";
	    String update = "UPDATE calorie_burned SET calorie_burned = calorie_burned + ? WHERE user_id = ? AND date_logged = ?";
	    String insert = "INSERT INTO calorie_burned (user_id, calorie_burned, date_logged, activity_name) VALUES (?, ?, ?, 'Walking')";

	    try {
	        Connection connection = DBConnection.getInstance().getConnection();

	        // Check if a record exists for the given user and date
	        PreparedStatement checkStmt = connection.prepareStatement(checkExistence);
	        checkStmt.setInt(1, walkLog.getUserId());
	        checkStmt.setDate(2, Date.valueOf(walkLog.getDateLogged())); // Assuming date_logged is a java.sql.Date
	        ResultSet rs = checkStmt.executeQuery();

	        if (rs.next()) {
	            // If record exists, update calorie_burned
	            PreparedStatement updateStmt = connection.prepareStatement(update);
	            updateStmt.setDouble(1, walkLog.getKmWalked()); // Assuming stored values are in km
	            updateStmt.setInt(2, walkLog.getUserId());
	            updateStmt.setDate(2, Date.valueOf(walkLog.getDateLogged()));
	            return updateStmt.executeUpdate() >= 1;
	        } else {
	            // If no record exists, insert a new entry
	            PreparedStatement insertStmt = connection.prepareStatement(insert);
	            insertStmt.setInt(1, walkLog.getUserId());
	            insertStmt.setDouble(2, walkLog.getKmWalked()); // Assuming direct km storage
	            insertStmt.setDate(2, Date.valueOf(walkLog.getDateLogged()));
	            return insertStmt.executeUpdate() >= 1;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	
}
