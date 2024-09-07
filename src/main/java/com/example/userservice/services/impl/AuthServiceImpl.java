package com.example.userservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.userservice.configurationsjwt.JwtUtils;
import com.example.userservice.dtos.request.DTOUserEntityRequest;
import com.example.userservice.exception.UserEntityNotCreatedException;
import com.example.userservice.models.LoginUser;
import com.example.userservice.services.AuthService;
import com.example.userservice.services.UserEntityService;

import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ReactiveAuthenticationManager authorizationManager;

    @Autowired
    private UserEntityService userEntityService;


    @Override
    public Mono<String> login(LoginUser loginUser) {
        return authorizationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.email(), loginUser.password()))
            .flatMap(auth -> {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                String jwt = jwtUtils.generateToken(userDetails.getUsername());
                return Mono.just(jwt);
            });
    }

    @Override
    public Mono<String> register(LoginUser loginUser) {
        return this.userEntityService.save(
                new DTOUserEntityRequest("Pending to Update", loginUser.email(), loginUser.password()))
            .switchIfEmpty(Mono.error(new UserEntityNotCreatedException("User not created")))
            .flatMap(user -> login(loginUser))
            .onErrorMap(ex -> new UserEntityNotCreatedException("Error creating user: " + ex.getMessage()));
    }
    
}
