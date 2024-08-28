package com.example.userservice.utils;


import com.example.userservice.models.UserEntity;
import com.example.userservice.repositories.UserEntityRepository;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Utils {

    @Bean
    public CommandLineRunner initData() {
        return args -> {

        };
    }

}
