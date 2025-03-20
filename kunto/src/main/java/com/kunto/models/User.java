package com.kunto.models;
import java.sql.Timestamp;
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private Timestamp createdAt;

    public User() {}

    public User(int id, String username, String passwordHash, String email, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.createdAt = createdAt;
    }
    
    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }  

    public int getId() {
    	return id; 
    }
    
    public void setId(int id) {
    	this.id = id; 
    }
    
    public String getUsername() {
    	return username; 
    }
    public void setUsername(String username){
    	this.username = username;
    }
    
    public String getPasswordHash() {
    	return passwordHash; 
    }
    
    public void setPasswordHash(String passwordHash) {
    	this.passwordHash = passwordHash;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public Timestamp getCreatedAt() {
    	return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
    	this.createdAt = createdAt;
    }
    
}
