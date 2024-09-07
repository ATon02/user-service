package com.example.userservice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.models.LoginUser;
import com.example.userservice.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Get Token Access", description = "Get a token access to system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token Access."),
            @ApiResponse(responseCode = "400", description = "Bad request: Credentials not valid or Authentication failed or Access Denied", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public Mono<String> authenticateUser(
        @RequestBody 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginUser.class)
            )
        ) LoginUser loginRequest) {
    return this.authService.login(loginRequest);
}

    @PostMapping(value = "/register")
    @Operation(summary = "Create User", description = "Save user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User saved correctly"),
            @ApiResponse(responseCode = "400", description = "Bad request: Email is duplicated or The email format is not valid or UserEntity Not Created or Check data contain empty fields", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public Mono<String> createUserEntity(
            @RequestBody() @Schema(example = "{\"email\": \"email@test.com\",\"password\": \"passwordtest\"}") LoginUser loginRequest) {
        return this.authService.register(loginRequest);
    }
}

