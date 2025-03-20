
package com.kunto.controller;


//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.*;
//import java.sql.*;
//
//@ServerEndpoint("/chat")
//public class ChatServer {
//    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
//    private static final Map<String, Integer> userIds = new HashMap<>();  
//
//    @OnOpen
//    public void onOpen(Session session) {
//        sessions.add(session);
//        System.out.println("New session connected: " + session.getId());
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) {
//       
//        String[] msgParts = message.split(":", 2);
//        String username = msgParts[0];  
//        String chatMessage = msgParts[1]; 
//
//        int senderId = userIds.get(username);
//
//        
//        saveChatMessage(senderId, chatMessage);
//
//        // Broadcast message to all connected sessions
//        for (Session s : sessions) {
//            try {
//                s.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void saveChatMessage(int senderId, String message) {
//      
//        try {
//        
//             
//        	Connection conn = Database.getConnection();
//        
//            String sql = "INSERT INTO chats (sender_id, receiver_id, message) VALUES (?, ?, ?)";
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setInt(1, senderId);
//                stmt.setInt(2, senderId);  
//                stmt.setString(3, message);
//                stmt.executeUpdate();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        sessions.remove(session);
//        System.out.println("Session disconnected: " + session.getId());
//    }
//}


import javax.management.loading.PrivateClassLoader;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.UserDAO;

@ServerEndpoint("/chat")
public class ChatServer {
    // Maps each session to a username (sender)
    private static final Map<Session, String> sessionUserMap = new ConcurrentHashMap<>();
    // Maps each username to a session for private messages
    private static final Map<String, Session> userSessionMap = new ConcurrentHashMap<>();
    // Maps group names to sets of sessions for group messages
    private static final Map<Integer, Set<Session>> groupSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("New session connected: " + session.getId());
       
        
        
    }

    @OnMessage
    public void onMessage(String message, Session session) {
    	System.out.print("private");
        try {
            JSONObject jsonMessage = new JSONObject(message);

            String type = jsonMessage.getString("type");

            if ("LOGIN".equals(type)) {
                // Handle login message to associate username with session
                String username = jsonMessage.getString("username");
                UserDAO userdao=UserDAO.getInstance();
                int userid=userdao.getUserIdFromUsername(username);
                boolean isComplete =userdao.notifyUserStatus("u"+userid,true,userSessionMap);
                System.out.println(isComplete);
                
                JSONArray groups = jsonMessage.getJSONArray("groups"); 
                System.out.println(groups);
                for (int i = 0; i < groups.length(); i++) {
                	JSONObject group = groups.getJSONObject(i);
                	String groupIdString = group.getString("id");  
                	int groupId = Integer.parseInt(groupIdString.substring(1));

                	System.out.println(groupId);
                	  groupSessions.putIfAbsent(groupId, ConcurrentHashMap.newKeySet()); // Initialize group if not exists
                      groupSessions.get(groupId).add(session);
                      System.out.println(groupSessions);
                     ;
                	System.out.println("Group ID: " + group.getString("id") + ", Group Name: " + group.getString("name"));
                }
                System.out.println(groupSessions.get(49));
                System.out.println(groupSessions);
                System.out.println(jsonMessage.get("groups"));
                sessionUserMap.put(session, username);
                userSessionMap.put(username, session);

                System.out.println("User logged in: " + username);
                session.getBasicRemote().sendText("Welcome, " + username + "!");

            }else if ("PRIVATE".equals(type)) {
            	System.out.print("private");
            	System.out.print(jsonMessage);
                handlePrivateMessage(jsonMessage, session);
            }else if ("GROUP".equalsIgnoreCase(type)) {
            	System.out.println(groupSessions);
                handleGroupMessage(jsonMessage, session);
            }else if ("NEW-GROUP".equalsIgnoreCase(type)) {
            	System.out.println("new group/.......");
            	sendGroupCreationNotification(jsonMessage,session);
            }
            else {
                System.out.println("Unknown message type: " + type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                session.getBasicRemote().sendText("Invalid message format!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
        
//       private void sendGroupCreationNotification(JSONObject jsonMessage, Session session) {
//    	   
//    	   JSONArray jsonArray=jsonMessage.getJSONArray("members");
//    	   JSONObject jObject=jsonMessage.getJSONObject("group");
//    	   
//       }
    
    
    private void sendGroupCreationNotification(JSONObject jsonMessage, Session session) {
        try {
            // Extract the list of group members and the group details
            JSONArray jsonArray = jsonMessage.getJSONArray("members");
            JSONObject groupDetails = jsonMessage.getJSONObject("group");
            int  groupId = Integer.parseInt(groupDetails.getString("id").substring(1));

            
            
            // The sender of the group creation
            String sender = sessionUserMap.get(session);

            // Create the notification message to be sent to the members
            JSONObject notificationMessage = new JSONObject();
            notificationMessage.put("type", "GROUP_CREATION");
            notificationMessage.put("sender", sender);
            notificationMessage.put("group", groupDetails); // Send the whole group details to users
            notificationMessage.put("message", sender + " created the group: " + groupDetails.getString("name"));
            
            addSessionToGroup(groupId, session);

            // Iterate through the list of group members and send the notification
            for (int i = 0; i < jsonArray.length(); i++) {
                String member = jsonArray.getString(i);

                // Get the session of the member from the userSessionMap
                Session memberSession = userSessionMap.get(member);
                

                if (memberSession != null) {
                	addSessionToGroup(groupId, memberSession);
                    try {
                        // Send the notification to the member if they are online
                        memberSession.getBasicRemote().sendText(notificationMessage.toString());
                        System.out.println("Sent group creation notification to " + member);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // If the member is offline, you could store the notification or notify later
                    System.out.println("User " + member + " is offline. Notification not sent.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                session.getBasicRemote().sendText("Error sending group creation notification.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void addSessionToGroup(int groupId, Session session) {
        groupSessions.computeIfAbsent(groupId, k -> new HashSet<>()).add(session);
    }


    private void handlePrivateMessage(JSONObject jsonMessage, Session session) {
        String target = jsonMessage.getString("target"); 
        String msg = jsonMessage.getString("message");
        String image = jsonMessage.getString("imageUrl");
        String sender = sessionUserMap.get(session); 
     
        if (sender == null) {
            try {
                session.getBasicRemote().sendText("You are not logged in!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Session targetSession = userSessionMap.get(target);
        if (targetSession != null) {
            try {
                JSONObject response = new JSONObject();
                response.put("type", "PRIVATE");
                response.put("sender", sender);
                response.put("message", msg);
                response.put("image", image);

                targetSession.getBasicRemote().sendText(response.toString());
                System.out.println("Sent private message from " + sender + " to " + target + ": " + msg);
//                System.out.println(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                session.getBasicRemote().sendText("User " + target + " is offline.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    private void handleGroupMessage(JSONObject jsonMessage, Session session) {
//        String groupName = jsonMessage.getString("target"); // Group name
//        String msg = jsonMessage.getString("message");
//        String sender = sessionUserMap.get(session); // Retrieve sender's username
//
//        if (sender == null) {
//            try {
//                session.getBasicRemote().sendText("You are not logged in!");
//                return;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        groupSessions.putIfAbsent(groupName, ConcurrentHashMap.newKeySet()); // Initialize group if not exists
//        groupSessions.get(groupName).add(session); // Add sender to the group
//
//        Set<Session> groupMembers = groupSessions.get(groupName);
//        if (groupMembers != null) {
//            for (Session s : groupMembers) {
//                try {
//                    JSONObject response = new JSONObject();
//                    response.put("type", "GROUP");
//                    response.put("sender", sender);
//                    response.put("message", msg);
//
//                    s.getBasicRemote().sendText(response.toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println("Broadcasted message to group " + groupName + " from " + sender + ": " + msg);
//        } else {
//            try {
//                session.getBasicRemote().sendText("Group " + groupName + " does not exist.");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    
    
    private void handleGroupMessage(JSONObject jsonMessage, Session session) {
    	System.out.println(jsonMessage);
        int groupId= jsonMessage.getInt("target"); 
        
        String msg = jsonMessage.getString("message");
        String image = jsonMessage.getString("imageUrl");
        String sender = sessionUserMap.get(session); // Retrieve sender's username

        if (sender == null) {
            try {
                session.getBasicRemote().sendText("You are not logged in!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

		/*
		 * groupSessions.putIfAbsent(groupId, ConcurrentHashMap.newKeySet()); //
		 * Initialize group if not exists groupSessions.get(groupId).add(session); //
		 * Add sender to the group
		 */
        Set<Session> groupMembers = groupSessions.get(groupId);
        System.out.println("groupsessions");
        System.out.println(groupSessions);
        groupSessions.get(groupId);
        if (groupMembers != null) {
            for (Session s : groupMembers) {
                try {
                	if(!session.equals(s)) {
                		JSONObject response = new JSONObject();
                        response.put("type", "GROUP");
                        response.put("groupId", groupId);
                        response.put("sender", sender);
                        response.put("message", msg);
                        response.put("image", image);
                        System.err.println(response);
                        s.getBasicRemote().sendText(response.toString());
                	}
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Broadcasted message to group " + groupId + " from " + sender + ": " + msg);
        } else {
            try {
                session.getBasicRemote().sendText("Group " + groupId + " does not exist.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
    	 String username = sessionUserMap.get(session);  // Retrieve username from session
    	    if (username != null) {
    	        userSessionMap.remove(username);  
    	        System.out.println("User disconnected: " + username);// Remove the session from active users
    	        UserDAO userdao=UserDAO.getInstance();
                int userid=userdao.getUserIdFromUsername(username);
                boolean isComplete =userdao.notifyUserStatus("u"+userid,false,userSessionMap);
    	        // Notify friends that this user is offline
    	       // int userId = getUserIdFromUsername(username); // Implement this function to fetch user ID from DB
//    	        if (userId != -1) {
//    	            notifyFriendsOffline(userId);
//    	        }
    	    }
      
        
            
        
        for (Set<Session> group : groupSessions.values()) {
            group.remove(session);
        }
        System.out.println("Session disconnected: " + session.getId());
    }
	/*
	 * private void notifyFriendsOffline(int userId) { Connection conn = null; conn
	 * = Database.getInstance().getConnection();
	 * 
	 * String query = "SELECT u.username FROM user_friends uf " +
	 * "JOIN users u ON u.id = (CASE WHEN uf.user_id = ? THEN uf.friend_id ELSE uf.user_id END) "
	 * + "WHERE uf.user_id = ? OR uf.friend_id = ?";
	 * 
	 * try (PreparedStatement stmt = conn.prepareStatement(query)) { stmt.setInt(1,
	 * userId); stmt.setInt(2, userId); stmt.setInt(3, userId);
	 * 
	 * ResultSet rs = stmt.executeQuery(); JSONObject offlineMessage = new
	 * JSONObject(); offlineMessage.put("type", "updateOnline");
	 * offlineMessage.put("id", "u" + userId); offlineMessage.put("online", false);
	 * 
	 * while (rs.next()) { String friendUsername = rs.getString("username"); if
	 * (userSessionMap.containsKey(friendUsername)) { Session friendSession =
	 * userSessionMap.get(friendUsername);
	 * friendSession.getBasicRemote().sendText(offlineMessage.toString()); } } }
	 * catch (SQLException | IOException e) { e.printStackTrace(); } }
	 * 
	 * private int getUserIdFromUsername(String username) { int userId = -1; //
	 * Default to -1 if not found Connection conn = null;
	 * 
	 * try { conn = Database.getInstance().getConnection(); String query =
	 * "SELECT id FROM users WHERE username = ?"; try (PreparedStatement stmt =
	 * conn.prepareStatement(query)) { stmt.setString(1, username); ResultSet rs =
	 * stmt.executeQuery(); if (rs.next()) { userId = rs.getInt("id"); } } } catch
	 * (SQLException e) { e.printStackTrace(); } return userId; }
	 */

	public static Map<String, Session> getUserSessionMap() {
		// TODO Auto-generated method stub
		return userSessionMap;
	}

	
	

	
}

