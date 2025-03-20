package com.kunto.models;

import java.security.Timestamp;

public class Group {
    private int id;
    private String groupName;
    private int createdBy;
    private Timestamp createdAt;

    // Constructors
    public Group() {}

    public Group(String groupName, int createdBy) {
        this.groupName = groupName;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
