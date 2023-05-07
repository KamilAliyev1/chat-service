package com.chatservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Slf4j
@Configuration
class StompErrorHandlerConfig {

    @Bean
    StompSubProtocolErrorHandler errorHandler() {
        return new StompSubProtocolErrorHandler() {
            @Override
            protected Message<byte[]> handleInternal(StompHeaderAccessor errorHeaderAccessor, byte[] errorPayload, Throwable cause, StompHeaderAccessor clientHeaderAccessor) {
                var message =  MessageBuilder.createMessage(cause.getCause().getMessage().getBytes(), errorHeaderAccessor.getMessageHeaders());
                log.info(message.toString());
                return message;
            }
        };
    }
}