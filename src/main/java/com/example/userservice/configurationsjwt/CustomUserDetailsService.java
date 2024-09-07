package com.example.userservice.configurationsjwt;


import com.example.userservice.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserEntityRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username){
        return userRepository.findByEmail(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User with email "+username+" not found")))
                .map(user -> new User(user.getEmail(), user.getPassword(), new ArrayList<>()));
    }

}