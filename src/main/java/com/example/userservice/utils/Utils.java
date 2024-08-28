package com.example.userservice.utils;



import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Utils {

    @Bean
    public CommandLineRunner initData() {
        return args -> {

        };
    }

}
