package com.kunto.models;

public class Post {
    private int id;
    private int userId;
    private String username;
    private String content;
    private String title;
    private String imageUrl;
    private String createdAt;
    private int views;

    // Constructors
    public Post() {}

    public Post(int id, int userId, String username, String content, String title, String imageUrl, String createdAt,int views) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.title = title;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.setViews(views);
    }
    public Post(int userId,  String content, String title, String imageUrl) {
        
        this.userId = userId;
       
        this.content = content;
        this.title = title;
        this.imageUrl = imageUrl;
        
    }


    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}
}

// Post