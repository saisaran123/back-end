

package com.kunto.controller;

//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//@WebServlet("/getChatMessages")
//public class GetChatMessagesServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int senderId = Integer.parseInt(request.getParameter("senderId"));
//        int receiverId = Integer.parseInt(request.getParameter("receiverId"));
//
//        Connection conn = null;
//		try {
//			conn = Database.getConnection();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        String query = "SELECT sender_id, receiver_id, message, timestamp FROM chats WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp ASC";
//        try (PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setInt(1, senderId);
//            stmt.setInt(2, receiverId);
//            stmt.setInt(3, receiverId);
//            stmt.setInt(4, senderId);
//
//            ResultSet rs = stmt.executeQuery();
//            JSONArray messagesArray = new JSONArray();
//
//            while (rs.next()) {
//                JSONObject message = new JSONObject();
//                message.put("sender", rs.getInt("sender_id"));
//                message.put("message", rs.getString("message"));
//                message.put("timestamp", rs.getString("timestamp"));
//                messagesArray.put(message);
//            }
//
//            response.setContentType("application/json");
//            response.getWriter().write(messagesArray.toString());
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }
//}
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.config.DBConnection;

//@WebServlet("/getChatHistory")
//public class GetChatHistoryServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        StringBuilder sb = new StringBuilder();
//        String line;
//        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//
//        String requestData = sb.toString();
//        try {
//            JSONObject json = new JSONObject(requestData);
//            int userId = json.getInt("userId");
//            int chatUserId = json.getInt("chatUserId");
//
//            Connection conn = Database.getConnection();
//            // Query to get the chat history between the user and the chatUser
//            String query = "SELECT c.sender_id, c.receiver_id, c.message, c.timestamp, u.username " +
//                           "FROM chats c " +
//                           "JOIN users u ON u.id = c.sender_id " +
//                           "WHERE (c.sender_id = ? AND c.receiver_id = ?) " +
//                           "OR (c.sender_id = ? AND c.receiver_id = ?) " +
//                           "ORDER BY c.timestamp ASC";
//
//            try (PreparedStatement stmt = conn.prepareStatement(query)) {
//                stmt.setInt(1, userId);
//                stmt.setInt(2, chatUserId);
//                stmt.setInt(3, chatUserId);
//                stmt.setInt(4, userId);
//
//                ResultSet rs = stmt.executeQuery();
//                JSONArray chatHistory = new JSONArray();
//
//                while (rs.next()) {
//                    JSONObject messageJson = new JSONObject();
//                    messageJson.put("senderId", rs.getInt("sender_id"));
//                    messageJson.put("receiverId", rs.getInt("receiver_id"));
//                    messageJson.put("message", rs.getString("message"));
//                    messageJson.put("timestamp", rs.getTimestamp("timestamp").toString());
//                    messageJson.put("senderUsername", rs.getString("username"));
//                    chatHistory.put(messageJson);
//                }
//
//                JSONObject responseJson = new JSONObject();
//                responseJson.put("status", "success");
//                responseJson.put("chatHistory", chatHistory);
//
//                response.setContentType("application/json");
//                response.getWriter().write(responseJson.toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//
//            JSONObject errorJson = new JSONObject();
//            errorJson.put("status", "error");
//            errorJson.put("message", "An error occurred while retrieving chat history.");
//            response.getWriter().write(errorJson.toString());
//        }
//    }

//}


@WebServlet("/getChatHistory")
public class GetChatHistoryServlet extends HttpServlet {
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
           // int userId = json.getInt("userId");
            HttpSession session=request.getSession(false);
            int userId = (int) session.getAttribute("user_id");
            int chatUserId = Integer.parseInt(json.getString("chatUserId"));
            System.out.println(userId+" "+chatUserId);

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
                String query = "SELECT c.sender_id, c.receiver_id, c.message, c.timestamp, c.image_url , u.username " +
                               "FROM chats c " +
                               "JOIN users u ON u.user_id = c.sender_id " +
                               "WHERE (c.sender_id = ? AND c.receiver_id = ?) " +
                               "OR (c.sender_id = ? AND c.receiver_id = ?) " +
                               "ORDER BY c.timestamp ASC";
               

                try (PreparedStatement stmt2 = conn.prepareStatement(query)) {
                    stmt2.setInt(1, userId);
                    stmt2.setInt(2, chatUserId);
                    stmt2.setInt(3, chatUserId);
                    stmt2.setInt(4, userId);

                    ResultSet rs2 = stmt2.executeQuery();
                    JSONArray chatHistory = new JSONArray();

                    while (rs2.next()) {
                        JSONObject messageJson = new JSONObject();
                        messageJson.put("senderId", rs2.getInt("sender_id"));
                        messageJson.put("receiverId", rs2.getInt("receiver_id"));
                        messageJson.put("image", rs2.getString("image_url"));
                        messageJson.put("message", rs2.getString("message"));
                        messageJson.put("timestamp", rs2.getTimestamp("timestamp").toString());
                        messageJson.put("senderUsername", rs2.getString("username"));
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

