package com.kunto.models;

import java.sql.Timestamp;





public class UserGroup {
    private int id;
    private int userId;
    private int groupId;
    private Timestamp joinedAt;

    public UserGroup() {}

    public UserGroup(int id, int userId, int groupId, Timestamp joinedAt) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
        this.joinedAt = joinedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }
}
