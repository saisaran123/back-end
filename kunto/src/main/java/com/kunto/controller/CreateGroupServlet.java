package com.kunto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.config.DBConnection;

@WebServlet("/createGroup")
public class CreateGroupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

      
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

       
        JSONObject jsonRequest = new JSONObject(sb.toString());
        String groupName = jsonRequest.optString("group _name");
       // int createdBy = jsonRequest.optInt("created_by", -1);
        HttpSession session=request.getSession(false);
        int createdBy  = (int) session.getAttribute("user_id");

        if (groupName == null || groupName.trim().isEmpty() || createdBy == -1) {
            out.print("{\"error\": \"Invalid request data\"}");
            return;
        }

        try { 
        	Connection conn = DBConnection.getInstance().getConnection();
          
            String insertGroupSQL = "INSERT INTO groups (group_name, created_by, created_at) VALUES (?, ?, ?) RETURNING id";
            try (PreparedStatement stmt = conn.prepareStatement(insertGroupSQL)) {
                stmt.setString(1, groupName);
                stmt.setInt(2, createdBy);
                stmt.setTimestamp(3, Timestamp.from(Instant.now()));

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int groupId = rs.getInt("id");

                   
                    String insertUserGroupSQL = "INSERT INTO user_groups (user_id, group_id) VALUES (?, ?)";
                    try (PreparedStatement userGroupStmt = conn.prepareStatement(insertUserGroupSQL)) {
                        userGroupStmt.setInt(1, createdBy);
                        userGroupStmt.setInt(2, groupId);
                        userGroupStmt.executeUpdate();
                    }

                    out.print("{\"success\": true, \"group_id\": " + groupId + "}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\": \"Database error\"}");
        }
    }
}
