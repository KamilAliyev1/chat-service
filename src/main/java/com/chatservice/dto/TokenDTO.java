package com.chatservice.dto;



public record TokenDTO(
        String accessToken,
        String refreshToken
) {
}
