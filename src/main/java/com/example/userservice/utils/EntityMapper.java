package com.example.userservice.utils;

import com.example.userservice.dtos.request.DTOUserEntityRequest;
import com.example.userservice.dtos.response.DTOUserEntityResponse;
import com.example.userservice.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserEntity dtoRequestToEntity(DTOUserEntityRequest userRequest) {
        UserEntity user = new UserEntity();
        user.setName(userRequest.getName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        return  user;
    }

    public DTOUserEntityResponse entityToDtoResponse(UserEntity user) {
        DTOUserEntityResponse dtoUserEntityResponse = new DTOUserEntityResponse();
        dtoUserEntityResponse.setId(user.getId());
        dtoUserEntityResponse.setName(user.getName());
        dtoUserEntityResponse.setEmail(user.getEmail());
        return  dtoUserEntityResponse;
    }

}
