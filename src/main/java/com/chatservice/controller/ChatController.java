package com.chatservice.controller;

import com.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/chats")
public class ChatController {


    private final ChatService chatService;


    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getMessagesByChat(@PathVariable Long id,@RequestParam Integer from){
        return ResponseEntity.ok(chatService.getMessages(id,from));
    }


}
