package com.chatservice.service;


import com.chatservice.model.Chat;
import com.chatservice.model.ChatType;
import com.chatservice.util.NotAccessToChatException;
import com.chatservice.util.NotValidChatException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class WebSocketAuthService {

    private final ChatService chatService;

    private final AuthenticationService authenticationService;

    private final UserServiceImpl userService;



    @SneakyThrows
    public void authPreSend(Message message) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        String to = accessor.getDestination();

        if(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equals("anonymous")
                && accessor.getCommand()!= StompCommand.DISCONNECT) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

            if (Strings.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
                throw new AuthenticationServiceException("");
            }

            String token = authorizationHeader.replace("Bearer ", "");

            authenticationService.authorize(token);

            accessor.setUser(SecurityContextHolder.getContext().getAuthentication());
        }
        switch (accessor.getCommand()){

            case SEND -> {

                to = to.replace("/chat/","");

                var chat = getChat(to);

                Long userID = Long.parseLong((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

                checkUserPermissionToChat(userID,chat);


            }
            case SUBSCRIBE -> {

                to = to.replace("/topic/","");

                Long userID = Long.parseLong((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

                checkUserPermissionToChat(userID,getChat(to));

            }
            case CONNECT -> {

                Long userID = Long.parseLong((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                userService.setUserActive(true,userID);

            }
            case DISCONNECT -> {

                Long userID = Long.parseLong((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                userService.setUserActive(false,userID);

            }

        }

        log.info(message.toString());
    }



    private Chat getChat(String to){

        Long chatId;

        try {
            chatId = Long.parseLong(to);
        }catch (NumberFormatException e){
            throw new NotValidChatException("this chat not exists");
        }

        Chat chat = chatService.getById(chatId);

        return chat;
    }

    private void checkUserPermissionToChat(Long userID,Chat chat){

        if(chat.getChatType()== ChatType.PUBLIC || chat.getMembers().stream().anyMatch(u->u.getID().equals(userID)))return;

        throw new NotAccessToChatException("not access to this chat");
    }

}
