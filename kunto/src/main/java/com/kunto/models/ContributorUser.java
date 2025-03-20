package com.kunto.models;

public class ContributorUser {
    private int userId;
    private String username;
    private int score;

    public ContributorUser(int userId, String username, int score) {
        this.userId = userId;
        this.username = username;
        this.score = score;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public int getScore() { return score; }
}
