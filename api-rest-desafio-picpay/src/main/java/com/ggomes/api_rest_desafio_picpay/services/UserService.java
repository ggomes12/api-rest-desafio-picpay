package com.ggomes.api_rest_desafio_picpay.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggomes.api_rest_desafio_picpay.entities.UserEntity;
import com.ggomes.api_rest_desafio_picpay.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<UserEntity> findByCpfCnpj(String cpfCnpj) {
        return userRepository.findByCpfCnpj(cpfCnpj);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
}
