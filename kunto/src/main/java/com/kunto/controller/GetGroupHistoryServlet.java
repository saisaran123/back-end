package com.kunto.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.config.DBConnection;

/**
 * Servlet implementation class GetGroupHistoryServlet
 */
@WebServlet("/getGroupHistory")

public class GetGroupHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String requestData = sb.toString();
        try {
            JSONObject json = new JSONObject(requestData);
            int groupId = (int) (json.getInt("groupId"));
            System.out.println(groupId);

            // Get the chatUserId based on the chatUsername
            Connection conn = DBConnection.getInstance().getConnection();
//            String getUserIdQuery = "SELECT id FROM users WHERE username = ?";
//            int chatUserId = -1;

//            try (PreparedStatement stmt = conn.prepareStatement(getUserIdQuery)) {
//                stmt.setString(1, chatUsername);
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next()) {
//                    chatUserId = rs.getInt("id");
//                }
//
//                if (chatUserId == -1) {
//                    JSONObject errorJson = new JSONObject();
//                    errorJson.put("status", "error");
//                    errorJson.put("message", "User not found.");
//                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                    response.getWriter().write(errorJson.toString());
//                    return;
//                }

                // Query to get the chat history between the user and the chatUser
                String query = "select u.username,gm.message,gm.timestamp ,gm.image_url from group_messages gm\n"
                		+ "JOIN users u ON gm.sender_id = u.user_id\n"
                		+ "WHERE gm.group_id = ?;";
               

                try (PreparedStatement stmt2 = conn.prepareStatement(query)) {
                    stmt2.setInt(1, groupId);
                   

                    ResultSet rs2 = stmt2.executeQuery();
                    JSONArray chatHistory = new JSONArray();

                    while (rs2.next()) {
                        JSONObject messageJson = new JSONObject();
                        messageJson.put("senderUsername", rs2.getString("username"));
                        messageJson.put("image", rs2.getString("image_url"));
                        messageJson.put("message", rs2.getString("message"));
                        messageJson.put("timestamp", rs2.getTimestamp("timestamp").toString());
                      
                        chatHistory.put(messageJson);
                    }

                    JSONObject responseJson = new JSONObject();
                    responseJson.put("status", "success");
                    responseJson.put("chatHistory", chatHistory);
                    response.setContentType("application/json");
                    response.getWriter().write(responseJson.toString());
                }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            JSONObject errorJson = new JSONObject();
            errorJson.put("status", "error");
            errorJson.put("message", "An error occurred while retrieving chat history.");
            response.getWriter().write(errorJson.toString());
        }
    }
}
