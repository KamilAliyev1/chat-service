package com.chatservice.service;


import com.chatservice.dto.Message;
import com.chatservice.mapper.MessageMapper;
import com.chatservice.model.Chat;
import com.chatservice.model.User;
import com.chatservice.repo.MessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
public class MessageService {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageRepo messageRepo;

    private final MessageMapper messageMapper = MessageMapper.INSTANCE;


    public void addMessage(Message message, String to){


        var messageEntity = messageMapper.toEntity(message);

        Long userId = Long.parseLong((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        messageEntity.setUser(User.builder().ID(userId).build());

        messageEntity.setChat(Chat.builder().ID(Long.parseLong(to)).build());

        messagingTemplate.convertAndSend("/topic/"+to,messageMapper.toDto(messageRepo.saveAndFlush(messageEntity)));


    }





}
