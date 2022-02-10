package com.example.chatapi.springbootfirebase.controller;

import com.example.chatapi.springbootfirebase.entity.Room;
import com.example.chatapi.springbootfirebase.entity.User;
import com.example.chatapi.springbootfirebase.service.UserService;
import com.google.api.client.json.Json;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public String saveUser(@RequestBody User user) throws ExecutionException, InterruptedException, FirebaseAuthException, MessagingException, IOException {
        return userService.saveUser(user);
    }

    @GetMapping("/users/{name}")
    public User getUserByName(@PathVariable String name) throws ExecutionException, InterruptedException {
        return userService.getUserByName(name);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        return userService.getAllUsers();
    }

    @PutMapping("/users")
    public String update(@RequestBody User user) throws ExecutionException, InterruptedException {
        return userService.updateUser(user);
    }

    @DeleteMapping("/users/{name}")
    public String deleteRoom(@PathVariable String name) throws ExecutionException, InterruptedException {
        return userService.deleteUser(name);
    }
}
