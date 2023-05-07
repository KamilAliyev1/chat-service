package com.chatservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPostDto(@NotBlank
                          @Email(message = "Email is not valid"
                                  , regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
                          String email
                         ,@Size(min = 8,max = 64)
                          @NotBlank
                          String password) {
}
