package com.kunto.models;

import java.security.Timestamp;

public class FAQAnswer {
    private int answerId;
    private int faqId;
    private int userId;
    private String answer;
    private String username;
    private boolean userLIked;
    private int likeCount;
    private java.sql.Timestamp createdAt;

    // Getters and setters
    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getFaqId() {
        return faqId;
    }

    public void setFaqId(int faqId) {
        this.faqId = faqId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp timestamp) {
        this.createdAt = timestamp;
    }

	public boolean isUserLIked() {
		return userLIked;
	}

	public void setUserLIked(boolean userLIked) {
		this.userLIked = userLIked;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
}
