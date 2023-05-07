package com.chatservice.mapper;

import com.chatservice.dto.MessageGET;
import com.chatservice.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);


    @Mapping(source = "ID",target = "id")
    @Mapping(target = "userId",source = "user.ID")
    MessageGET toDto(Message message);


    Message toEntity(com.chatservice.dto.Message message);
}