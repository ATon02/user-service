package com.example.userservice.services;

import com.example.userservice.models.LoginUser;

import reactor.core.publisher.Mono;

public interface AuthService {
    
    Mono<String> login(LoginUser loginUser);
    Mono<String> register(LoginUser loginUser);

}
