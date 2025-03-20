package com.kunto.controller;


//import java.io.BufferedReader;
//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.json.JSONObject;
//import org.json.JSONTokener;
//
//@WebServlet("/login")
//public class LoginServlet extends HttpServlet {
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//    	
//    	System.out.println("login..........");
//    	 BufferedReader reader = request.getReader();
//         JSONObject json = new JSONObject(new JSONTokener(reader));
//         
//        String username = json.getString("username");
//        String password = json.getString("password");
//        System.out.println(username+"  "+password);
//        // Example: Replace this with real DB validation
//        if ("Bob".equals(username) && "password123".equals(password)) {
//            HttpSession session = request.getSession();
//            session.setAttribute("user_id", 2);
//            session.setMaxInactiveInterval(300); // 30 minutes session timeout
//
//            response.setContentType("application/json");
//            response.getWriter().write("{\"success\": true, \"message\": \"Login successful\"}");
//        } else {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("{\"success\": false, \"message\": \"Invalid credentials\"}");
//        }
//    }
//}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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

import org.json.JSONObject;

import com.kunto.config.DBConnection;
import com.kunto.models.User;
import com.kunto.util.PasswordUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	
    	
    	PrintWriter out = response.getWriter();

        
        StringBuilder data = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            data.append(line);
        }
        JSONObject json = new JSONObject(data.toString());
        String email = json.getString("email");
       
        String password = json.getString("password");

      

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            response.getWriter().println("Email and password are required.");
            return;
        }
        
        try {
        	Connection conn = DBConnection.getInstance().getConnection();
        	JSONObject jsonResponse = new JSONObject();
            String query = "SELECT password_hash FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHashedPassword = rs.getString("password_hash");
                String enteredHashedPassword = PasswordUtil.hashPassword(password);
               // String enteredHashedPassword = password;

                if (storedHashedPassword.equals(enteredHashedPassword)) {
                	 User user=getUserIDAndNamefromEmail(email);
                    HttpSession session = request.getSession();
                    session.setAttribute("user_id",user.getId());
                    session.setAttribute("username",user.getUsername());
                    System.out.println(user.getId());
                    session.setMaxInactiveInterval(24*24*60);
                    response.sendRedirect("kunto.html");
                } else {
                	 
                	jsonResponse.put("Invalid email or password.",false);
                	out.print(jsonResponse.toString());
                }
                
            } else {
            	jsonResponse.put("Invalid email or password.",false);
            	out.print(jsonResponse.toString());
            }
        } catch (SQLException e) {
        	JSONObject errorResponse = new JSONObject();
            e.printStackTrace();
            errorResponse.put("Login error.",false);
            out.print(errorResponse.toString());
        }
    }
    
    public User getUserIDAndNamefromEmail(String email) {
    	try{
    		Connection conn = DBConnection.getInstance().getConnection();
            String query = "SELECT user_id ,username FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String userName =rs.getString("username");
                User user = new User(id,userName);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
           return null;
        }
		return null;
    	
		
	}
    
    
}
