package com.chatservice.mapper;

import com.chatservice.dto.ChatGET;
import com.chatservice.dto.MessageGET;
import com.chatservice.model.Chat;
import com.chatservice.model.Message;
import com.chatservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);
    
    @Mapping(source = "ID",target = "id")
    @Mapping(target = "members",source = "members" ,qualifiedByName = "members")
    @Mapping(target = "messages",source = "messages",qualifiedByName = "messages")
    ChatGET toDTO(Chat chat);


    @Named("messages")
    default List<MessageGET> map(List<Message> messages){
        return messages.stream().map(MessageMapper.INSTANCE::toDto).collect(Collectors.toList());
    }
    
    
    @Named("members")
    default List<Long> map(Set<User> members){
        return members.stream().map(User::getID).collect(Collectors.toList()); 
    }

}
