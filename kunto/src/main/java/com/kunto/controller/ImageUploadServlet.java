package com.kunto.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.websocket.Session;
//
//import com.kunto.util.Database;
//
//@WebServlet("/up")
//@MultipartConfig
//public class ImageUploadServlet5 extends HttpServlet {
//
//    private static final String UPLOAD_DIR = "uploads";
//    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String uploadPath = "/home/vish-zstch-1435/eclipse-workspace/kunto/uploads/";
//
//        File uploadDir = new File(uploadPath);
//        if (!uploadDir.exists()) {
//            uploadDir.mkdir();
//        }
//
//        try {
//            Part filePart = request.getPart("image");
//            String fileName = filePart.getSubmittedFileName();
//            String filePath = uploadPath + File.separator + fileName;
//
//            try (InputStream inputStream = filePart.getInputStream();
//                 FileOutputStream outputStream = new FileOutputStream(filePath)) {
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//            }
//
//            // Image URL to be stored in the database
//            String imageUrl = request.getContextPath() + "/" + UPLOAD_DIR + "/" + fileName;
//
//            // Store the image URL in the database (assuming userId and messageId are available)
//            storeImageUrlInDatabase(imageUrl);
//
//            // Send the image URL to the WebSocket sessions for broadcasting
//            for (Session session : sessions) {
//                session.getAsyncRemote().sendText("New image shared: " + imageUrl);
//            }
//
//            response.getWriter().write("Image uploaded successfully: " + fileName);
//
//        } catch (Exception e) {
//            response.getWriter().write("Failed to upload image: " + e.getMessage());
//        }
//    }
//
//    private void storeImageUrlInDatabase(String imageUrl) {
//        // Example to store the image URL in the database
//        String query = "INSERT INTO chat_messages (user_id, message, image_url) VALUES (?, ?, ?)";
//        try (Connection conn = Database.getInstance().getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            int userId = 1;  // Assume the user ID is 1 for this example
//            String message = "User uploaded an image";  // Example message
//
//            stmt.setInt(1, userId);
//            stmt.setString(2, message);
//            stmt.setString(3, imageUrl);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}


@WebServlet("/upload-image")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024, // 1 MB
	    maxFileSize = 1024 * 1024 * 10, // 10 MB
	    maxRequestSize = 1024 * 1024 * 15 // 15 MB
	)

public class ImageUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        String uploadDirPath = getServletContext().getRealPath("/uploads/");
        
       
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();  
        }

        try {
           
            Part filePart = request.getPart("image");
            String fileName = sanitizeFileName(filePart.getSubmittedFileName());
            String filePath = uploadDirPath + File.separator + fileName;

           
            try (InputStream inputStream = filePart.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

           
            String imageUrl = request.getContextPath() + "/uploads/" + fileName;

           
            response.setContentType("application/json");
            response.getWriter().write("{\"imageUrl\": \"" + imageUrl + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to upload image: " + e.getMessage() + "\"}");
        }
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_.-]", "");
    }
}

