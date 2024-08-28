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

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserEntityRepository entityRepository ;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            String email = "demo2@demo.com";
            String name = "User 1";
            String password = "020202020";

            entityRepository.findByEmail(email)
                .flatMap(existingUser -> Mono.error(new RuntimeException("User already exists")))
                .switchIfEmpty(
                    Mono.defer(() -> {
                        UserEntity newUser = new UserEntity(name, email, passwordEncoder.encode(password));
                        return Mono.just(entityRepository.save(newUser));
                    })
                )
                .subscribe(
                    user -> System.out.println("User created successfully: " + name),
                    error -> System.err.println("Error occurred: " + error.getMessage())
                );
        };
    }

}
