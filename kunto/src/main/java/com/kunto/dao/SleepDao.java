package com.kunto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import com.kunto.config.DBConnection;
import com.kunto.models.Sleep;

public class SleepDao {
    private Connection connection;
    private static SleepDao sleepDao;

    
    public void setConnection() {
        try {
            connection = DBConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SleepDao getInstance() {
        if (sleepDao == null) {
            sleepDao = new SleepDao();
        }
        return sleepDao;
    }

    public boolean insertSleepData(Sleep sleep) {
        String query = "INSERT INTO sleep_tracker (user_id, sleep_time, wake_time, logged_at) VALUES (?, ?, ?, ?)";
        String checkString = "SELECT * FROM sleep_tracker WHERE user_id = ? AND logged_at = ?";
        String update = "UPDATE sleep_tracker SET sleep_time = ?, wake_time = ? WHERE user_id = ? AND logged_at = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            PreparedStatement ps2 = connection.prepareStatement(checkString);
            PreparedStatement ps3 = connection.prepareStatement(update);

            // Convert Java objects to SQL types
            int userId = sleep.getUserId();
            Time sleepTime = Time.valueOf(sleep.getSleepTime());
            Time wakeTime = Time.valueOf(sleep.getWakeTime());
            java.sql.Date loggedAt = java.sql.Date.valueOf(sleep.getCreatedAt());

            System.out.println("Checking sleep data for user: " + userId + " on " + loggedAt);

            // Check if sleep entry already exists for today
            ps2.setInt(1, userId);
            ps2.setDate(2, loggedAt);
            ResultSet rs1 = ps2.executeQuery();

            if (rs1.next()) {
                System.out.println("Existing record found. Updating...");
                ps3.setTime(1, sleepTime);
                ps3.setTime(2, wakeTime);
                ps3.setInt(3, userId);
                ps3.setDate(4, loggedAt);

                if (ps3.executeUpdate() >= 1) {
                    return true;
                }
            } else {
                System.out.println("No existing record found. Inserting new...");
                ps.setInt(1, userId);
                ps.setTime(2, sleepTime);
                ps.setTime(3, wakeTime);
                ps.setDate(4, loggedAt);

                if (ps.executeUpdate() >= 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
