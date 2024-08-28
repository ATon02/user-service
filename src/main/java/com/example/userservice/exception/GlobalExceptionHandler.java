package com.example.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserEntityNotFoundException.class)
    public Mono<ResponseEntity<String>> handleUserEntityNotFoundException(UserEntityNotFoundException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
    }

    @ExceptionHandler(UserEntityNotCreatedException.class)
    public Mono<ResponseEntity<String>> handleUserEntityNotCreatedException(UserEntityNotCreatedException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }

    @ExceptionHandler(UserEntityNotUpdatedException.class)
    public Mono<ResponseEntity<String>> handleUserEntityNotUpdatedException(UserEntityNotUpdatedException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }

    @ExceptionHandler(NotValidFieldException.class)
    public Mono<ResponseEntity<String>> handleEmptyFieldException(NotValidFieldException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }

    @ExceptionHandler(InvalidUserException.class)
    public Mono<ResponseEntity<String>> handleUserEntityNotFoundException(InvalidUserException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }
}