package com.vinsguru.userservice.service;

import com.vinsguru.userservice.dto.UserDto;
import com.vinsguru.userservice.repository.UserRepository;
import com.vinsguru.userservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Flux<UserDto> all(){
        return this.userRepository.findAll()
                    .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> getUserById(final int userId){
        return this.userRepository.findById(userId)
                    .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> createUser(Mono<UserDto> userDtoMono){
        return userDtoMono
                    .map(EntityDtoUtil::toEntity)
                    .flatMap(this.userRepository::save)
                    .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> updateUser(int id, Mono<UserDto> userDtoMono){
        return this.userRepository.findById(id)
                .flatMap(u -> userDtoMono
                                .map(EntityDtoUtil::toEntity)
                                .doOnNext(e -> e.setId(id)))
                .flatMap(this.userRepository::save)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<Void> deleteUser(int id){
        return this.userRepository.deleteById(id);
    }

}
