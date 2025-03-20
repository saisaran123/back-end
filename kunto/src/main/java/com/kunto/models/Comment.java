package com.kunto.models;

import java.sql.Timestamp;

public class Comment {
    private int postId;
    private int userId;
    private String comment;
    private String userName;
    private Timestamp createAt;

    public Comment(int postId, int userId, String comment) {
        this.postId = postId;
        this.userId = userId;
        this.comment = comment;
    }
    public Comment(int postId, int userId, String comment,String userName,Timestamp createAt) {
        this.postId = postId;
        this.userId = userId;
        this.comment = comment;
        this.setUserName(userName);
        this.setCreateAt(createAt);
    }

    public int getPostId() {
        return postId;
    }

    public int getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }
	public Timestamp getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}