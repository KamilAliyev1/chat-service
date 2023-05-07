package com.chatservice.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthFilter authFilter;


    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET,"/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/swagger-ui.html/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/swagger-ui/**").permitAll()
                .requestMatchers("/chat/**").permitAll()
                .requestMatchers("/v1/**").permitAll()
                .requestMatchers("/login-page.html").permitAll()
                .requestMatchers("/chat-page/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/styles/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        ;



        log.info("SecurityFilterChain CREATED");

        return http.build();

    }


}
