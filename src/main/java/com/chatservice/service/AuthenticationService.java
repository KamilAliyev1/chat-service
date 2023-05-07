package com.chatservice.service;

import com.chatservice.dto.LoginRequest;
import com.chatservice.dto.TokenDTO;
import com.chatservice.dto.UserGetDto;
import com.chatservice.model.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AuthenticationService {


    private final TokenService  tokenService;

    private final UserServiceImpl userSecurityDetailServiceImpl;

//    private final UserBanRepo userBanRepo;

    private final PasswordEncoder passwordEncoder;


    TokenDTO map(TokenPair tokenPair){
        return new TokenDTO(tokenPair.getAccessToken(),tokenPair.getRefreshToken());
    }

    public void authorize(String token){

        Map<String ,Object > userdetails = tokenService.verifyJws(token);


        Long id = Long.parseLong(
                String.valueOf(
                        userdetails.get("sub")
                )
        );

        tokenService.validate(
                id
                ,token
        );

        List<String > auths = (List<String>) userdetails.get("auths");

        List<? extends GrantedAuthority> grantedAuthorities = auths.stream().map(e->new SimpleGrantedAuthority(e)).collect(Collectors.toList());

        var user = userSecurityDetailServiceImpl.findById(id);

        if(!user.isEnabled()) throw new DisabledException("account disabled");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                id.toString()
                , null
                , grantedAuthorities
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    public TokenDTO fromRefreshToken(String token){

        Map<String ,Object> map = tokenService.verifyJws(token);

        Long id = Long.parseLong(
                String.valueOf(
                        map.get("sub")
                )
        );

        tokenService.validate(
                id
                ,token
        );


        var getDTO = userSecurityDetailServiceImpl.findById(id);

        if(!getDTO.isEnabled()) throw new DisabledException("account disabled");


        var tokenPair =  tokenService.getTokenPairs(
                id
                ,  null
                ,getDTO.auths().stream().map(t->t.getPermission()).collect(Collectors.toList())
        );

        tokenService.saveToken(tokenPair);

        return map(tokenPair);

    }

    public UserGetDto validateTokenFromDB(Long id, String token){
        if(id==null){
            Map<String,Object> map = tokenService.verifyJws(token);
            id = Long.parseLong(String.valueOf(map.get("sub")));
            tokenService.validate(id,token);
            return userSecurityDetailServiceImpl.findById(id);
        }
        tokenService.validate(id,token);

        return userSecurityDetailServiceImpl.findById(id);
    }


    public TokenDTO fromCredential(LoginRequest loginRequest){


        var authentication = userSecurityDetailServiceImpl.findByEmail(loginRequest.username());


        if (!authentication.isEnabled()) throw new DisabledException("account disabled");

        try {
            if(!passwordEncoder.matches(loginRequest.password(), authentication.getPassword()))
                throw new BadCredentialsException("wrong password");
        }catch (AuthenticationException e){
//            UserBanControl(authentication);
            throw  e;
        }

//        var userBan = new UserBan();
//
//        userBan.setID(authentication.getID());
//
//        userBanRepo.save(userBan);

        var token = tokenService.getTokenPairs(
                authentication.getID()
                ,""
                , authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
        );

        tokenService.saveToken(token);

        return map(token);


    }


//    void UserBanControl(UserSecurityDetail detail){
//        Short temp = 1;
//
//        var optional = userBanRepo.findById(detail.getID());
//
//        if(optional.isPresent() && optional.get().getNum()!=null){
//
//            temp= (short) (optional.get().getNum().shortValue()+1);
//
//            if(temp>=5){
//
//                userSecurityDetailServiceImpl.setDisable(detail.getID());
//
//                userSecurityDetailServiceImpl.sendEmailVerification(detail);
//
//                userBanRepo.deleteById(detail.getID());
//
//                return;
//            }
//        }
//        var userBan = new UserBan();
//        userBan.setID(detail.getID());
//        userBan.setNum(temp);
//        userBanRepo.save(userBan);
//    }




}
