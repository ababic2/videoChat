package com.example.chatapi.springbootfirebase.controller;

import com.example.chatapi.springbootfirebase.entity.Room;
import com.example.chatapi.springbootfirebase.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

//@RestController is a special controller used in RESTFul web services and the equivalent of @Controller + @ResponseBody
@RestController
@RequestMapping("/api")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/rooms")
    public String saveRoom(@RequestBody Room room) throws ExecutionException, InterruptedException {
        return roomService.saveRoom(room);
    }

    @GetMapping("/rooms/{name}")
    public Room getRoomByName(@PathVariable String name) throws ExecutionException, InterruptedException {
        return roomService.getRoomByName(name);
    }

    @GetMapping("/rooms")
    public List<Room> getAllRooms() throws ExecutionException, InterruptedException {

        return roomService.getAllRooms();
    }

    @PutMapping("/rooms")
    public String update(@RequestBody Room room) throws ExecutionException, InterruptedException {
        return roomService.updateRoom(room);
    }

    @DeleteMapping("/rooms/{name}")
    public String deleteRoom(@PathVariable String name) throws ExecutionException, InterruptedException {
        return roomService.deleteRoom(name);
    }
}
