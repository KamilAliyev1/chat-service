package com.chatservice.service;

import com.chatservice.dto.MessageGET;
import com.chatservice.mapper.ChatMapper;
import com.chatservice.mapper.MessageMapper;
import com.chatservice.model.Chat;
import com.chatservice.repo.ChatRepo;
import com.chatservice.repo.MessageRepo;
import com.chatservice.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ChatService implements com.chatservice.util.Service<Chat,Chat> {

    private final ChatRepo chatRepo;

    private final MessageRepo messageRepo;

    private final ChatMapper chatMapper = ChatMapper.INSTANCE;

    private MessageMapper messageMapper = MessageMapper.INSTANCE;
    @Override
    public Chat getById(Long ID) {

        var optional = chatRepo.findById(ID);

        if(optional.isEmpty())throw new NotFoundException();

        return optional.get();

    }


    public List<MessageGET> getMessages(Long id,Integer from){
        return messageRepo.findByChatId(
                Chat.builder().ID(id).build()
                , PageRequest.of(from,10))
                .stream().map(messageMapper::toDto).collect(Collectors.toList());
    }


}
