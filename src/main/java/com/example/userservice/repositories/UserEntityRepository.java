package com.example.userservice.repositories;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.userservice.models.UserEntity;
import reactor.core.publisher.Mono;


public interface UserEntityRepository extends ReactiveCrudRepository<UserEntity, Long> {

    Mono<UserEntity> findByEmail(String email);

}