package com.chatservice.controller;


import com.chatservice.dto.UserGetDto;
import com.chatservice.dto.UserPostDto;
import com.chatservice.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import java.util.List;


@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;


    @GetMapping("/chats")
    ResponseEntity<List<?>> getChats(){
        return ResponseEntity.ok(userServiceImpl.getChatsByCurrentUserId());
    }

    @GetMapping
    ResponseEntity<?> getUser(){
        return ResponseEntity.ok(userServiceImpl.getCurrentUser());
    }


    @PostMapping
    public ResponseEntity<UserGetDto> add(@RequestBody UserPostDto userPostDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userServiceImpl.save(userPostDto));
    }

    private final SimpUserRegistry simpUserRegistry;
    @GetMapping("/v")
    void z(){
        System.out.println("viiyy");
        var users = simpUserRegistry.getUsers().stream().toList();
        users.get(0).getName();
        System.out.println(simpUserRegistry.getUserCount());

    }
    @Autowired
    WebSocketMessageBrokerStats webSocketMessageBrokerStats;





}
