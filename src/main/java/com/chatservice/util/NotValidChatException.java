package com.chatservice.util;

public class NotValidChatException extends RuntimeException{
    public NotValidChatException() {
    }

    public NotValidChatException(String message) {
        super(message);
    }
}
