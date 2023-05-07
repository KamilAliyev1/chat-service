package com.chatservice.dto;

import com.chatservice.model.ChatType;

import java.util.List;

public record ChatGET(Long id, ChatType chatType, List<Long> members, List<MessageGET> messages) {
}
