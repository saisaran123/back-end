package com.kunto.models;

import java.time.LocalDate;

public class WalkLog {
    private int userId;
    private LocalDate dateLogged;
    private double kmWalked;

    // Constructor
    public WalkLog() {}

    public WalkLog(int userId, LocalDate dateLogged, double kmWalked) {
        this.userId = userId;
        this.dateLogged = dateLogged;
        this.kmWalked = kmWalked;
    }
    
    // Getters and Setters


    public int getUserId() {
        return userId;
    }


    public LocalDate getDateLogged() {
        return dateLogged;
    }


    public double getKmWalked() {
        return kmWalked;
    }




}
