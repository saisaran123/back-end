package com.kunto.models;

import java.time.LocalTime;

import com.kunto.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Sleep {


    private int user_id;

    private LocalTime sleepTime;

    private LocalTime wakeTime;

    
    private LocalDate today;

    // Constructors
    public Sleep() {}

    public Sleep(int user_id, LocalTime sleepTime, LocalTime wakeTime,LocalDate today) {
        this.user_id = user_id;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.today=today;
    }

    // Getters and Setters
    public int getUserId() { return user_id; }
    public LocalTime getSleepTime() { return sleepTime; }
    public LocalTime getWakeTime() { return wakeTime; }
    public LocalDate getCreatedAt() { return today; }
}
