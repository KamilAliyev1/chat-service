package com.chatservice.service;

import com.chatservice.dto.ChatGET;
import com.chatservice.dto.UserGetDto;
import com.chatservice.dto.UserPostDto;
import com.chatservice.mapper.ChatMapper;
import com.chatservice.model.User;
import com.chatservice.model.UserAuthority;
import com.chatservice.repo.UserRepo;
import com.chatservice.util.NotFoundException;
import com.chatservice.util.UniqueException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class UserServiceImpl {

    private final UserRepo userRepo;

    private final ChatService chatService;

    private final PasswordEncoder passwordEncoder;

    private final ChatMapper chatMapper;

    public List<ChatGET> getChatsByCurrentUserId(){

        Long id = Long.parseLong((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        var optional = userRepo.findById(id);

        return optional.get().getChats().stream().map(chatMapper::toDTO).toList();

    }

    public UserGetDto getCurrentUser(){
        return map(userRepo.findById(Long.parseLong((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())).get());
    }

    @Transactional
    public UserGetDto save(UserPostDto obj) {

        if(userRepo.existsByEmail(obj.email()))throw new UniqueException();


        User user = User.builder()
                .email(obj.email())
                .password(passwordEncoder.encode(obj.password()))
                .grantedAuths(Set.of(UserAuthority.builder().ID(1L).build()))
                .isEnabled(true)// TODO: 4/25/2023 heleki true
                .build();


        var entity = userRepo.saveAndFlush(user);

//        sendEmailVerification(entity);

        return map(user);

    }




    public UserGetDto findById(Long id) {

        var temp = userRepo.findById(id);

        if(temp.isEmpty())throw new NotFoundException();

        User detail = temp.get();

        return map(detail);

    }
    public User findByEmail(String email){
        var optional = userRepo.findByEmail(email);

        if(optional.isEmpty()) throw new NotFoundException();

        return optional.get();

    }

    UserGetDto map(User user){
        return new UserGetDto(user.getID(), user.getEmail(), user.isEnabled(),user.getGrantedAuths().stream().toList());
    }


    @Transactional
    void setUserActive( Boolean b, Long id){
        userRepo.setUserActive(b,id);
    }





}
