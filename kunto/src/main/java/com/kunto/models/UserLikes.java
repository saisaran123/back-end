package com.kunto.models;
public class UserLikes {
    private int userId;
    private String username;
    private int totalLikes;

    public UserLikes(int userId, String username, int totalLikes) {
        this.userId = userId;
        this.username = username;
        this.totalLikes = totalLikes;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public int getTotalLikes() { return totalLikes; }
}
