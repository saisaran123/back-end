package com.kunto.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.json.JSONObject;

import com.kunto.config.DBConnection;

public class OtherActivitiesDAO {
    public boolean insertActivity(int user_id, JSONObject reqJson) {
        String insert = "INSERT INTO calorie_burned (user_id, calorie_burned, date_logged, activity_name, workout_time) VALUES (?, ?, ?, ?, CAST(? AS INTERVAL))";
        String check = "SELECT calorie_burned, workout_time FROM calorie_burned WHERE user_id = ? AND date_logged = ? AND activity_name = ?";
        String update = "UPDATE calorie_burned SET calorie_burned = calorie_burned + ?, workout_time = workout_time + CAST(? AS INTERVAL) WHERE user_id = ? AND date_logged = ? AND activity_name = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try (PreparedStatement checkStmt = conn.prepareStatement(check)) {
            // Convert String date to java.sql.Date
            String activityDateStr = reqJson.getString("activityDate");
            LocalDate localDate = LocalDate.parse(activityDateStr);
            Date activityDate = Date.valueOf(localDate);

            String activityName = reqJson.optString("activityName", "Workout"); 
            double activityCal = reqJson.getDouble("activityCal");
            String workoutTime = reqJson.optString("TimeTaken", "00:00:00");

            checkStmt.setInt(1, user_id);
            checkStmt.setDate(2, activityDate);
            checkStmt.setString(3, activityName);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Entry exists with the same activity name, update calories and workout time
                    try (PreparedStatement updateStmt = conn.prepareStatement(update)) {
                        updateStmt.setDouble(1, activityCal);
                        updateStmt.setString(2, workoutTime);
                        updateStmt.setInt(3, user_id);
                        updateStmt.setDate(4, activityDate);
                        updateStmt.setString(5, activityName);
                        return updateStmt.executeUpdate() > 0;
                    }
                } else {
                    // No existing record with the same activity name, insert new
                    try (PreparedStatement insertStmt = conn.prepareStatement(insert)) {
                        insertStmt.setInt(1, user_id);
                        insertStmt.setDouble(2, activityCal);
                        insertStmt.setDate(3, activityDate);
                        insertStmt.setString(4, activityName);
                        insertStmt.setString(5, workoutTime);
                        return insertStmt.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
