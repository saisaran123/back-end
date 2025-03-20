package com.kunto.controller;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/checkSession")
public class CheckSessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false); // Check existing session
        resp.setContentType("application/json");

        if (session != null && session.getAttribute("user_id") != null) {
             int username = (int) session.getAttribute("user_id");
            resp.getWriter().write("{\"loggedIn\": true, \"user_id\": \"" + username + "\"}");
        } else {
          //  resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"loggedIn\": false}");
        }
    }
}
