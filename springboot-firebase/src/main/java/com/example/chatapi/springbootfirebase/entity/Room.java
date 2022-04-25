package com.example.chatapi.springbootfirebase.entity;


import javax.annotation.Generated;
import java.util.ArrayList;

public class Room {
    private String id;

    private String name;


    public Room() {
    }

    public Room(String name, ArrayList<User> users) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}