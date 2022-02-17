package com.example.chatapi.springbootfirebase.controller;

import com.example.chatapi.springbootfirebase.entity.Room;
import com.example.chatapi.springbootfirebase.entity.User;
import com.example.chatapi.springbootfirebase.entity.UserRoom;
import com.example.chatapi.springbootfirebase.service.UserRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class UserRoomController {

    @Autowired
    private UserRoomService userRoomService;

    @PostMapping("/join")
    public String saveUserInRoom(@RequestBody UserRoom userRoom) throws ExecutionException, InterruptedException {
        return userRoomService.addUserToRoom(userRoom);
    }

    @DeleteMapping("/leave/{name}") // or remove from room?
    public String deleteUserFromRoom(@PathVariable String name) throws ExecutionException, InterruptedException {
        return userRoomService.deleteUserFromRoom(name);
    }

    // get members for specific room
    @GetMapping("/members/{name}")
    public List<User> getMembersForRoom(@PathVariable String name, @RequestHeader("Authorization") String token) throws ExecutionException, InterruptedException {
        return userRoomService.getMembersForRoom(name, token);
    }

    @GetMapping("/myrooms/{name}")
    public List<Room> getRoomsForUser(@PathVariable String name) throws ExecutionException, InterruptedException {
        return userRoomService.getRoomsForUser(name);
    }
}
