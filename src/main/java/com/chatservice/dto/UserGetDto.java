package com.chatservice.dto;

import com.chatservice.model.UserAuthority;

import java.util.List;

public record UserGetDto(Long id, String email, Boolean isEnabled, List<UserAuthority> auths) {
}
