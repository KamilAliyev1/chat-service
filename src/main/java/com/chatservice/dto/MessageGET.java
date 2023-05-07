package com.chatservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MessageGET(
        Long id,
        String message,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm")
        LocalDateTime creationTime

        ,Long userId
) {
}
