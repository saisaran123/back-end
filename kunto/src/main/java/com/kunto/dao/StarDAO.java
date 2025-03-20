package com.kunto.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.kunto.config.DBConnection;

public class StarDAO {
	private static StarDAO instance;
		
	private StarDAO(){}
    public static StarDAO getInstance(){
    	 if (instance == null) {
             instance = new StarDAO();
         }
         return instance;
    }
 
    public int getUserStars(int userId){
        String query = "SELECT stars FROM user_star_history WHERE user_id = ?";
        try  {
        	Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stars");
                }
            }
            catch(SQLException e) {
            	e.printStackTrace();
            }
        }
        catch(SQLException e) {
        	e.printStackTrace();
        }
        return 6;     
      }

   
    public void updateUserStars(int userId, int stars) {
    	
    	    String query = "UPDATE user_star_history SET stars = ?, last_active_date = CURRENT_DATE WHERE user_id = ?";
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, stars);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public int calculateAndUpdateStars(int userId) throws SQLException {
        int currentStars = getUserStars(userId);
        LocalDate lastActive = getLastActiveDate(userId);
        LocalDate today = LocalDate.now();

        if (lastActive != null) {
            long daysMissed = ChronoUnit.DAYS.between(lastActive, today);

            if (daysMissed == 0) { 
             
                return currentStars;
            }

            System.out.println("Days Missed: " + daysMissed);
            System.out.println("Current Stars Before Calculation: " + currentStars);

            if (daysMissed > 1) {
               
                currentStars = Math.max(0, currentStars - (int) (daysMissed - 1));
            }
        }

        
        if (currentStars < 6) {
            currentStars++;
        }

        updateUserStars(userId, currentStars);
        return currentStars;
    }

    public LocalDate getLastActiveDate(int userId) {
        String query = "SELECT last_active_date FROM user_star_history WHERE user_id = ?";
        try {
        	Connection conn =DBConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("last_active_date").toLocalDate();
                }
            }
        }catch(SQLException e) {
        	e.printStackTrace();
        }
        return null;
    }
     
}
