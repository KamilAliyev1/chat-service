package com.chatservice.util;

public class TokenVerifyException extends RuntimeException{
    public TokenVerifyException() {
    }

    public TokenVerifyException(String message) {
        super(message);
    }
}
