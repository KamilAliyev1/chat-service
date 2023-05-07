package com.chatservice.controller;


import com.chatservice.dto.Message;
import com.chatservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RestController
@CrossOrigin
public class StompController {

    private final MessageService messageService;


    @MessageMapping("/chat/{to}")
    public void chat(@Payload Message message, @DestinationVariable String to){
        messageService.addMessage(message,to);
    }

}
