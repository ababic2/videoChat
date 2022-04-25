package com.example.chatapi.springbootfirebase.entity;

import com.google.cloud.Timestamp;

public class Message {
    private String content;
    private Timestamp createdAt;
    private String roomId;
    private String userId;
    private String username;

    public Message() {
    }

    public Message(String content, Timestamp timestamp, String roomId, String userId, String username) {
        this.content = content;
        this.createdAt = timestamp;
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
