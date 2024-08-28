package com.example.userservice.exception;

public class UserEntityNotUpdatedException extends RuntimeException {
    public UserEntityNotUpdatedException(String message) {
        super(message);
    }
}