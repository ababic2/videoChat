package com.example.chatapi.springbootfirebase.controller;

import com.example.chatapi.springbootfirebase.entity.Message;
import com.example.chatapi.springbootfirebase.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/messages")
    public String saveMessage(@RequestBody Message message) throws ExecutionException, InterruptedException {
        return messageService.saveMessage(message);
    }

    @GetMapping("/messages/{name}")
    public List<Message> getAllMessagesInRoom(@PathVariable String name) throws ExecutionException, InterruptedException {
        return messageService.getAllMessagesInRoom(name);
    }

    @PutMapping("/messages")
    public String update(@RequestBody Message message) throws ExecutionException, InterruptedException {
        return messageService.updateMessage(message);
    }

    @DeleteMapping("/messages/{name}")
    public String deleteRoom(@PathVariable String name) throws ExecutionException, InterruptedException {
        return messageService.deleteMessage(name);
    }
}
