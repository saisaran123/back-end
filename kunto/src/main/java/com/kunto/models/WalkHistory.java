package com.kunto.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WalkHistory {
    private int userId;
    private double kmWalked;
    private LocalDateTime dateLogged;

    // Constructor
    public WalkHistory() {}

    public WalkHistory(int userId, double kmWalked, LocalDateTime dateLogged) {
        this.userId = userId;
        this.kmWalked = kmWalked;
        this.dateLogged = dateLogged;
    }


    public int getUserId() {
        return userId;
    }



    public double getKmWalked() {
        return kmWalked;
    }



    public LocalDateTime getDateLogged() {
        return dateLogged;
    }


 

}
