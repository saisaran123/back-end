package com.kunto.model;

import java.security.Timestamp;

public class Chat {
    private int id;
    private int senderId;
    private int receiverId;
    private String message;
    private Timestamp timestamp;

    public Chat() {}

    public Chat(int id, int senderId, int receiverId, String message, Timestamp timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getId(){
    	return id; 
    }
    
    public void setId(int id){
    	this.id = id; 
    }
    
    public int getSenderId(){
    	return senderId; 
    }
    
    public void setSenderId(int senderId){
    	this.senderId = senderId; 
    }
    
    public int getReceiverId(){
    	return receiverId;
    }
    
    public void setReceiverId(int receiverId){
    	this.receiverId = receiverId; 
    }
    
    public String getMessage() {
    	return message; 
    }
    
    public void setMessage(String message){
    	this.message = message;
    }
    
    public Timestamp getTimestamp(){
    	return timestamp; 
    }
    
    public void setTimestamp(Timestamp timestamp) {
    	this.timestamp = timestamp; 
    }
}