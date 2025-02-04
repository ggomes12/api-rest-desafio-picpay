package com.ggomes.api_rest_desafio_picpay.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ggomes.api_rest_desafio_picpay.entities.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByCpfCnpj(String cpfCnpj);
    Optional<UserEntity> findByEmail(String email);
}