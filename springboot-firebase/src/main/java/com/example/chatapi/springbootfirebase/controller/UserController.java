package com.example.chatapi.springbootfirebase.controller;

import com.example.chatapi.springbootfirebase.entity.User;
import com.example.chatapi.springbootfirebase.service.UserService;
import com.example.chatapi.springbootfirebase.utils.LoginInfo;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class UserController{
    @Autowired
    private UserService userService;

    @PostMapping("/register")// done
    public String saveUser(@RequestBody User user) throws ExecutionException, InterruptedException, IOException {
        return userService.registerUser(user);
    }

    @PostMapping("/login") // done
    public String logInUser(@RequestBody LoginInfo userLoginInfo) {
        return userService.logIn(userLoginInfo);
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
    public Object getUserByName(@RequestHeader("Authorization") String token) {
        return userService.checkIfUserIsVerified(token);
    }

    @GetMapping("/users")
    public List<User> getAllUsers(@RequestHeader("Authorization") String token) throws ExecutionException, InterruptedException {
        return userService.getAllUsers(token);
    }

    @PutMapping("/settings")
    public String update(@RequestHeader("Authorization") String token, @RequestParam String username, @RequestParam String password, @RequestParam String email) throws ExecutionException, FirebaseAuthException, InterruptedException, IOException {
        return userService.updateUser(username, password, email, token);
    }

    @PostMapping("/revokeToken")
    public String revokeToken(@RequestParam String uid) throws FirebaseAuthException {
        System.out.println(uid);
        return userService.revokeToken(uid);
    }

    @DeleteMapping("/users/{documentName}") // done
    public String deleteUser(@PathVariable String documentName, @RequestHeader("Authorization") String token) {
        return userService.deleteUser(documentName, token);
    }
}
