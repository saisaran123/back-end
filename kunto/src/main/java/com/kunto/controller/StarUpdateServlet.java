package com.kunto.controller;


import java.io.IOException;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.kunto.dao.StarDAO;

@WebServlet("/updateStars")
public class StarUpdateServlet extends HttpServlet {
   

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	 HttpSession session=request.getSession(false);
         int userId = (int) session.getAttribute("user_id");

        try {
        	StarDAO starDao=StarDAO.getInstance();
			int updatedStars = starDao.calculateAndUpdateStars(userId);
            response.getWriter().write("{\"success\": true, \"stars\": " + updatedStars + "}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false}");
        }
    }
}
