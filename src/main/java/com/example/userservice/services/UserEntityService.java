package com.example.userservice.services;


import com.example.userservice.dtos.request.DTOUserEntityRequest;
import com.example.userservice.dtos.response.DTOUserEntityResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserEntityService {

    Mono<DTOUserEntityResponse> getById(long id);

    Flux<DTOUserEntityResponse> getAll();

    Mono<Object> save(DTOUserEntityRequest userEntityRequest);

    Mono<Void> deleteById(Long id);

    Mono<DTOUserEntityResponse> update(Long id,DTOUserEntityRequest userEntity);

}
