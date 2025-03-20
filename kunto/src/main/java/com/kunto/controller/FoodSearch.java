package com.kunto.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/searchRecipe")
public class FoodSearch extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");

        // Get and encode recipe parameter
        String recipe = URLEncoder.encode(req.getParameter("recipe"), StandardCharsets.UTF_8);

        System.out.println("API Request for: " + recipe);

        try (PrintWriter out = res.getWriter()) {  // Auto-close writer
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)  // Use HTTP/2 for better performance
                    .build();

            String apiUrl = "https://api.edamam.com/api/recipes/v2?type=public&q=" + recipe +
                    "&app_id=900da95e&app_key=40698503668e0bb3897581f4766d77f9";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check API response
            if (response.statusCode() == 200) {
                out.println(response.body());
            }  else {
                res.setStatus(response.statusCode());
                out.println("{\"error\": \"Failed to fetch recipe data\"}");
            }

        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
