package com.example.userservice.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class DTOUserEntityResponse {
    @Schema(example = "0")
    private Long id;
    @Schema(example = "Name Test")
    private String name;
    @Schema(example = "Email Tests")
    private String email;

    public DTOUserEntityResponse() {
    }

    public DTOUserEntityResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
