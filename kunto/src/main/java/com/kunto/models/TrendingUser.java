package com.kunto.models;
public class TrendingUser {
    private int userId;
    private String username;
    private int engagementScore;

    public TrendingUser(int userId, String username, int engagementScore) {
        this.userId = userId;
        this.username = username;
        this.engagementScore = engagementScore;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public int getEngagementScore() { return engagementScore; }
}
