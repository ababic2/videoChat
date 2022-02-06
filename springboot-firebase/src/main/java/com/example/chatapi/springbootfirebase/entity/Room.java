package com.example.chatapi.springbootfirebase.entity;

import java.util.ArrayList;

public class Room {
    private String name;

    public Room() {
    }

    public Room(String name, ArrayList<User> users) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}