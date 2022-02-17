package com.example.chatapi.springbootfirebase.controller;

import com.example.chatapi.springbootfirebase.entity.User;
import com.example.chatapi.springbootfirebase.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class UserController{
    @Autowired
    private UserService userService;

    @PostMapping("/register")// done
    public String saveUser(@RequestBody User user) throws ExecutionException, InterruptedException, FirebaseAuthException, MessagingException, IOException {
        return userService.registerUser(user);
    }

    @PostMapping("/login") // done
    public String logInUser(@RequestBody User user) {
        return userService.logIn(user);
    }

    @PostMapping("/verification") // done
    public String verification(@RequestHeader("Authorization") String token) {
        System.out.println(token);
        return userService.verification(token);
    }

    @GetMapping("/users/{name}") // done
    public Object getUserByName(@PathVariable String name, @RequestHeader("Authorization") String token) throws ExecutionException, InterruptedException {
        return userService.getUserByName(name, token);
    }

    @GetMapping("/userinfo") // done
    public Object getUserByName(@RequestHeader("Authorization") String token) throws ExecutionException, InterruptedException {
        return userService.checkIfUserIsVerified(token);
    }

    @GetMapping("/users")
    public List<User> getAllUsers(@RequestHeader("Authorization") String token) throws ExecutionException, InterruptedException {
        return userService.getAllUsers(token);
    }

    @PutMapping("/users") // done
    public String update(@RequestBody User user, @RequestHeader("Authorization") String token) throws ExecutionException, InterruptedException, FirebaseAuthException {
        return userService.updateUser(user, token);
    }

    @DeleteMapping("/users/{documentName}") // done
    public String deleteUser(@PathVariable String documentName, @RequestHeader("Authorization") String token) {
        return userService.deleteUser(documentName, token);
    }
}
