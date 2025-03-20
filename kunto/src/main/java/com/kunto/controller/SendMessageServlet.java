
package com.kunto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.kunto.config.DBConnection;

@WebServlet("/sendChatMessage")
public class SendMessageServlet extends HttpServlet {
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
           
            System.out.println(json);
           // int senderId = json.getInt("senderId");
            HttpSession session=request.getSession(false);
            int senderId = (int) session.getAttribute("user_id");
            int receiverId = json.getInt("receiverId");
            String message = json.getString("message");
            String image =json.getString("image");

            Connection conn = DBConnection.getInstance().getConnection();
            String query = "INSERT INTO chats (sender_id, receiver_id, message ,image_url) VALUES (?, ?, ? ,?)";
            try {
            	PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, senderId);
                stmt.setInt(2, receiverId);
                stmt.setString(3, message);
                stmt.setString(4, image);
                
                stmt.execute();

                JSONObject responseJson = new JSONObject();
                responseJson.put("status", "success");
                response.setContentType("application/json");
                response.getWriter().write(responseJson.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
