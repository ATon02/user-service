package com.example.userservice.utils;

import com.example.userservice.dtos.request.DTOUserEntityRequest;
import com.example.userservice.exception.InvalidUserException;
import com.example.userservice.models.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern patternEmail = Pattern.compile(EMAIL_PATTERN);

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\|,.<>\\/?])(?=.*\\d)(?=.*[a-zA-Z]).{8,}$";
    private static final Pattern patternPassword = Pattern.compile(PASSWORD_PATTERN);


    @Override
    public boolean supports(Class<?> clazz) {
        return UserEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DTOUserEntityRequest user = (DTOUserEntityRequest) target;

        if (user.getName() == null || user.getName().isBlank()) {
            errors.rejectValue("name", "user.name.blank", "The 'name' field is empty");
            throw new InvalidUserException("The 'name' field is empty");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            errors.rejectValue("email", "user.email.blank", "The 'email' field is empty");
            throw new InvalidUserException("The 'email' field is empty");
        }

        if (!validEmail(user.getEmail())) {
            errors.rejectValue("email", "user.email.invalid.format", "The 'email' field is not valid format");
            throw new InvalidUserException("The 'email' field is not valid format");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            errors.rejectValue("password", "user.password.blank", "The 'password' field is empty");
            throw new InvalidUserException("The 'password' field is empty");
        }

        if (!validPassword(user.getPassword())) {
            errors.rejectValue("password", "user.password.invalid.format", "The 'password' field is not valid format");
            throw new InvalidUserException("The 'password' field is not valid format");
        }

    }

    private boolean validEmail(String email) {
        if (email != null) {
            Matcher matcher = patternEmail.matcher(email);
            return matcher.find();
        }
        return false;
    }

    private boolean validPassword(String password) {
        if (password != null) {
            Matcher matcher = patternPassword.matcher(password);
            return matcher.find();
        }
        return false;
    }

    public Mono<Void> validateDto(Object target) {
        try {
            Errors errors = new BeanPropertyBindingResult(target, "user");
            validate(target, errors);
            if (errors.hasErrors()) {
                return Mono.error(new InvalidUserException("Validation errors occurred"));
            }
            return Mono.empty();
        } catch (InvalidUserException e) {
            return Mono.error(e);
        }
    }
}
