package com.chatservice.controller;



import com.chatservice.dto.LoginRequest;
import com.chatservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController("TokeControllerV1")
@RequestMapping("/v1/tokens")
public class TokenController {

    private final AuthenticationService authenticationService;

    @PostMapping("/refresh")
    public ResponseEntity<?> fromRefreshToken(@RequestParam String token){
        return ResponseEntity.ok(authenticationService.fromRefreshToken(token));
    }


    @PostMapping("/credential")
    public ResponseEntity<?> fromUserCredentials(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authenticationService.fromCredential(loginRequest));
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/validate")
    public ResponseEntity<?> validateTokenFromDB(@RequestParam(required = false) Long id,@RequestParam String token){
        return ResponseEntity.ok(authenticationService.validateTokenFromDB(id,token));
    }

}