package com.kunto.controller;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.util.Utils;

@WebServlet("/logWater")
public class WaterLoggerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("heyyy");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {

            JSONObject jsonRequest = Utils.getJsonObject(request);
        	HttpSession session=request.getSession(false);

        	int userId=(int) session.getAttribute("user_id");
            String type = jsonRequest.getString("type");
            // Add 250ml water entry to the database
            Connection conn = DBConnection.getInstance().getConnection();
            String insertQuery = "INSERT INTO user_water_log (user_id, water_drank ,change_type) VALUES (?, 250,?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setInt(1, userId);
            stmt.setString(2, type);
            stmt.executeUpdate();

            stmt.close();

            // Response JSON
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "successfull");
            out.print(jsonResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Failed to log water intake: " + e.getMessage());
            out.print(errorResponse.toString());
        }
    }
}
