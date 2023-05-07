package com.chatservice.config;


import com.chatservice.service.WebSocketAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class WebSocketFilter implements ChannelInterceptor {


    private final WebSocketAuthService webSocketAuthService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        webSocketAuthService.authPreSend(message);
        return message;
    }

}
