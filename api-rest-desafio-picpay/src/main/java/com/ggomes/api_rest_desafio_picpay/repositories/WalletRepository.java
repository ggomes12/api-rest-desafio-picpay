package com.ggomes.api_rest_desafio_picpay.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ggomes.api_rest_desafio_picpay.entities.WalletEntity;


@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    Optional<WalletEntity> findByUserId(Long userId);
}