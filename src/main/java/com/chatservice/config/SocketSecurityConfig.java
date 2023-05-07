package com.chatservice.config;


import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;


//@EnableWebSocketSecurity
@Configuration
public class SocketSecurityConfig  extends AbstractSecurityWebSocketMessageBrokerConfigurer {


    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {

        messages.anyMessage().permitAll();

    }

//    @Bean
//    AuthorizationManager<Message<?>> authorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
//
//
//
//        messages.simpTypeMatchers(
//                SimpMessageType.CONNECT
//                ,SimpMessageType.MESSAGE
//                ,SimpMessageType.SUBSCRIBE
//                ,SimpMessageType.UNSUBSCRIBE
//                ,SimpMessageType.DISCONNECT
//                ,SimpMessageType.CONNECT_ACK
//                ,SimpMessageType.HEARTBEAT
//                ,SimpMessageType.DISCONNECT_ACK
//                ,SimpMessageType.OTHER
//        ).permitAll()
//        .simpSubscribeDestMatchers("/topic/**").permitAll()
//        .anyMessage().permitAll()
//        .simpDestMatchers("/chat/**").permitAll();
//        return messages.build();
//    }

}
