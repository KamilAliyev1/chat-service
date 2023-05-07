package com.chatservice.util;

public class UniqueException extends RuntimeException{
    public UniqueException() {
    }

    public UniqueException(String message) {
        super(message);
    }
}
