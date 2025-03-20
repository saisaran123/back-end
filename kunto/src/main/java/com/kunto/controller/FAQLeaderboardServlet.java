
package com.kunto.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kunto.dao.FAQDao;
import com.kunto.models.ContributorUser;
@WebServlet("/faqLeaderBoard")
public class FAQLeaderboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String type = request.getParameter("type"); // "topContributors" or "mostLiked"

        try (PrintWriter out = response.getWriter()) {
            FAQDao dao =FAQDao.getInstance();
            List<ContributorUser> users;

            if ("topContributors".equals(type)) {
                users = dao.getTopContributors(5);
            } else if ("mostLiked".equals(type)) {
                users = dao.getMostLikedUsers(5);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            JSONArray jsonArray = new JSONArray();
            for (ContributorUser user : users) {
                JSONObject userObj = new JSONObject();
                userObj.put("userId", user.getUserId());
                userObj.put("username", user.getUsername());
                userObj.put("count", user.getScore());
                jsonArray.put(userObj);
            }

            JSONObject data = new JSONObject();
            data.put("success", true);
            data.put("data", jsonArray);
            // Write JSON response
            
            out.print(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}