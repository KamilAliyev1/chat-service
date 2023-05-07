package com.chatservice.service;


import com.chatservice.config.TokenConfigProperties;
import com.chatservice.model.TokenPair;
import com.chatservice.model.TokenType;
import com.chatservice.repo.TokenRepo;
import com.chatservice.util.TokenVerifyException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepo tokenRepo;

    private final RSAKey rsaKey;

    private final TokenConfigProperties customTokenConfigProperties;


    private RSASSAVerifier rsassaVerifier;


    @SneakyThrows
    @PostConstruct
    void init(){
        rsassaVerifier = new RSASSAVerifier(rsaKey);
    }


    @SneakyThrows
    public JWT generateJws(Map<String,Object> claims) {

        SignedJWT jwt = new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS256)
                , JWTClaimsSet.parse(claims)
        );

        jwt.sign(new RSASSASigner(rsaKey.toPrivateKey()));

        return jwt;
    }


    public TokenPair getTokenPairs(Long subscriber, String audience, List<String > authorities){

        return new TokenPair(
                subscriber
                ,getJwt(subscriber.toString(),audience,authorities, TokenType.ACCESS).serialize()
                ,getJwt(subscriber.toString(),audience,authorities,TokenType.REFRESH).serialize()
        );
    }



    public JWT getJwt(String subscriber, String audience, List<String > authorities, TokenType tokenType){

        long exp = Instant.now().plusSeconds(tokenType.getValidTime()).getEpochSecond();

        Map<String,Object> claims = new HashMap<>();

        claims.put("authorities",authorities);
        claims.put("iss",customTokenConfigProperties.getIssuer());
        claims.put("sub",subscriber);
        claims.put("aud",audience);
        claims.put("nbf", Instant.now().getEpochSecond());
        claims.put("exp", exp);
        claims.put("iat", Instant.now().getEpochSecond());

        var jwt = generateJws(claims);

        return jwt;
    }


    @SneakyThrows
    public Map<String ,Object > verifyJws(String jwt) {

        SignedJWT signedJWT = SignedJWT.parse(jwt);

        var check = signedJWT.verify(rsassaVerifier);

        if(!check) throw new TokenVerifyException("illegal token");

        var iss = signedJWT.getJWTClaimsSet().getIssuer();

        var exp = signedJWT.getJWTClaimsSet().getExpirationTime();

        var sub = signedJWT.getJWTClaimsSet().getSubject();

        var nbf = signedJWT.getJWTClaimsSet().getNotBeforeTime();

        if(!iss.equals(customTokenConfigProperties.getIssuer()))throw new TokenVerifyException("token not from issuer");

        if(Instant.now().isBefore(nbf.toInstant()))throw new TokenVerifyException("nbf");

        if(Instant.now().isAfter(exp.toInstant()))throw new TokenVerifyException("exp");

        var auths  = signedJWT.getJWTClaimsSet().getStringArrayClaim("authorities");

        Map<String ,Object> map = new HashMap<>();

        map.put("sub",sub);

        map.put("auths",List.of(auths));

        return map;
    }


    public void saveToken(TokenPair tokenPair){
        tokenRepo.save(tokenPair);
    }

    public void validate(Long id,String token) {

        var tokenPair = tokenRepo.findById(id);

        if(tokenPair.isEmpty())throw new TokenVerifyException("This token not valid");

        if(
                !tokenPair.get().getAccessToken().equals(token)
                && !tokenPair.get().getRefreshToken().equals(token)
        ) throw new TokenVerifyException("This token not valid");

    }


}
