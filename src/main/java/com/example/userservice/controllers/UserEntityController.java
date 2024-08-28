package com.example.userservice.controllers;

import com.example.userservice.dtos.request.DTOUserEntityRequest;
import com.example.userservice.dtos.response.DTOUserEntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.userservice.exception.UserEntityNotFoundException;
import com.example.userservice.services.UserEntityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserEntityController {
    @Autowired
    private UserEntityService userEntityService;

    @GetMapping("/{id}")
    @Operation(summary = "Get User By Id", description = "Return a User id to get.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User data found."),
            @ApiResponse(responseCode = "400", description = "Bad request: User with ID not found", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public Mono<DTOUserEntityResponse> getUserEntityById(@PathVariable Long id) {
        return userEntityService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Get All Users", description = "Return all Users of system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users data found."),
            @ApiResponse(responseCode = "400", description = "Bad request: Not found Users",content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public Flux<DTOUserEntityResponse> getAllUsers() {
        return userEntityService.getAll();
    }

    @PostMapping
    @Operation(summary = "Create User", description = "Save User in the system. **Warning:** Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User saved correctly", content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = DTOUserEntityResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request: User Not Created or Bad request: The field is empty or User not created due to duplicate email or The field is not valid format", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public Mono<Object> createUserEntity(@RequestBody DTOUserEntityRequest UserEntity) {
        return userEntityService.save(UserEntity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update User By Id", description = "Update user in the system. **Warning:** Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated correctly"),
            @ApiResponse(responseCode = "400", description = "Bad request: User not found or Bad request: The field not is valid or or User not created due to duplicate email or The field is not valid format", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public Mono<DTOUserEntityResponse> updateUserEntity(@RequestBody DTOUserEntityRequest UserEntity,
                                             @PathVariable Long id) {
        return userEntityService.update(id,UserEntity);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User By Id", description = "Delete User in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User delete."),
            @ApiResponse(responseCode = "400", description = "Bad request: User not found", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public Mono<ResponseEntity<Object>> deleteUserEntity(@PathVariable Long id) {
        return userEntityService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(UserEntityNotFoundException.class, ex -> Mono.just(ResponseEntity.badRequest().body(ex.getMessage())));
    }
}
