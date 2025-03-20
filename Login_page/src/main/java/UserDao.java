package com.kunto.dao;

import com.kunto.config.DBConnection;
import com.kunto.model.user_details;
import java.sql.*;

public class UserDao {

    public boolean registerUser(user_details user) {
        String sql = "INSERT INTO user_details (goal, height, weight, level, activity_level, gender, dob, country, city, user_name,user_id) VALUES (? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
        	Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getGoal());
            stmt.setInt(2, user.getHeight());
            stmt.setInt(3, user.getWeight());
            stmt.setString(4, user.getLevel());
            stmt.setString(5, user.getActiveLevel());
            stmt.setString(6, user.getGender());
            if (user.getDateOfBirth() != null && !user.getDateOfBirth().isEmpty()) {
                stmt.setDate(7, Date.valueOf(user.getDateOfBirth()));
            } else {
                stmt.setNull(7, Types.DATE);
            }
            stmt.setString(8, user.getCountry());
            stmt.setString(9, user.getCity());
            stmt.setString(10, user.getUsername());
            stmt.setInt(11, 1);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}