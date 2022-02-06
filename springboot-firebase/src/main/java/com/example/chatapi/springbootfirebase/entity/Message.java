package com.example.chatapi.springbootfirebase.entity;

import com.google.cloud.Timestamp;

public class Message {
    private String content;
    private Timestamp timestamp;
    private String roomId;
    private String userId;

    public Message() {
    }

    public Message(String content, Timestamp timestamp, String roomId, String userId) {
        this.content = content;
        this.timestamp = timestamp;
        this.roomId = roomId;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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
}
