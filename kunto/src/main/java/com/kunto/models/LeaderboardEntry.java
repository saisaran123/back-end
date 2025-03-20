package com.kunto.models;

public class LeaderboardEntry {
	private String userName;
    private int userId;
    private int postOrQuestionCount;

    public LeaderboardEntry(String userName, int userId, int postOrQuestionCount) {
        this.userId = userId;
        this.postOrQuestionCount = postOrQuestionCount;
        this.setUserName(userName);
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostOrQuestionCount() {
        return postOrQuestionCount;
    }

    public void setPostOrQuestionCountt(int postOrQuestionCount) {
        this.postOrQuestionCount = postOrQuestionCount;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}