package com.chatservice.util;

public class NotAccessToChatException extends RuntimeException{
    public NotAccessToChatException() {
    }

    public NotAccessToChatException(String message) {
        super(message);
    }
}
