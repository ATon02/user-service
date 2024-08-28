package com.example.userservice.services.impl;

import com.example.userservice.dtos.request.DTOUserEntityRequest;
import com.example.userservice.dtos.response.DTOUserEntityResponse;
import com.example.userservice.exception.InvalidUserException;
import com.example.userservice.utils.EntityMapper;
import com.example.userservice.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.exception.UserEntityNotCreatedException;
import com.example.userservice.exception.UserEntityNotFoundException;
import com.example.userservice.repositories.UserEntityRepository;
import com.example.userservice.services.UserEntityService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<DTOUserEntityResponse> getById(long id) {
        return userEntityRepository.findById(id)
                .map(entityMapper::entityToDtoResponse)
                .switchIfEmpty(Mono.error(new UserEntityNotFoundException("User with ID " + id + " not found")));
    }

    @Override
    public Flux<DTOUserEntityResponse> getAll() {
        return userEntityRepository.findAll()
                .map(entityMapper::entityToDtoResponse)
                .switchIfEmpty(Flux.error(new UserEntityNotFoundException("Not found Users")));
    }

    @Override
    public Mono<DTOUserEntityResponse> save(DTOUserEntityRequest userEntityRequest) {
        return userEntityRepository.findByEmail(userEntityRequest.getEmail())
          .flatMap(existingUser -> Mono.error(new UserEntityNotCreatedException("User not created due to duplicate email")))
          .switchIfEmpty(saveMethod(userEntityRequest))
          .map(savedUser -> (DTOUserEntityResponse)savedUser)
          .onErrorMap(InvalidUserException.class,ex -> new UserEntityNotCreatedException(ex.getMessage()));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return userEntityRepository.findById(id)
          .switchIfEmpty(Mono.error(new UserEntityNotFoundException("User with id " + id + " not found")))
          .flatMap(userEntity -> {userEntityRepository.delete(userEntity).subscribe();
              return Mono.empty();
          });
    }

    @Override
    public Mono<DTOUserEntityResponse> update(Long id, DTOUserEntityRequest userEntity) {
        return userEntityRepository.findByEmail(userEntity.getEmail())
           .flatMap(existingUser -> {
             if (!existingUser.getId().equals(id)) {
                 return Mono.error(new UserEntityNotCreatedException("User not updated due to duplicate email"));
             }
             return updatedMethod(id, userEntity);
           })
           .switchIfEmpty(updatedMethod(id, userEntity))
           .onErrorMap(InvalidUserException.class, ex -> new UserEntityNotCreatedException(ex.getMessage()));
    }

    private Mono<DTOUserEntityResponse> updatedMethod(Long id, DTOUserEntityRequest userEntity) {
        return userValidator.validateDto(userEntity)
            .then(userEntityRepository.findById(id)
                .flatMap(existUserEntity -> {
                    existUserEntity.setName(userEntity.getName());
                    existUserEntity.setEmail(userEntity.getEmail());
                    existUserEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
                    return userEntityRepository.save(existUserEntity)
                        .map(entityMapper::entityToDtoResponse)
                        .switchIfEmpty(Mono.error(new UserEntityNotCreatedException("User not updated")));
                }).switchIfEmpty(Mono.error(new UserEntityNotFoundException("User with ID " + id + " not found")))
            )
            .onErrorMap(DuplicateKeyException.class, ex -> new UserEntityNotCreatedException("User not updated due to duplicate email"));
    }

    private Mono<DTOUserEntityResponse> saveMethod(DTOUserEntityRequest userEntity) {
        return userValidator.validateDto(userEntity)
            .then(userEntityRepository.save(entityMapper.dtoRequestToEntity(userEntity))
                .map(entityMapper::entityToDtoResponse)
                .switchIfEmpty(Mono.error(new UserEntityNotCreatedException("User not saved")))
            )
            .onErrorMap(DuplicateKeyException.class, ex -> new UserEntityNotCreatedException("User not saved due to duplicate email"));
    }

}
