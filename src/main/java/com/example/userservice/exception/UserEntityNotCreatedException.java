package com.example.userservice.exception;

public class UserEntityNotCreatedException extends RuntimeException {
    public UserEntityNotCreatedException(String message) {
        super(message);
    }
}