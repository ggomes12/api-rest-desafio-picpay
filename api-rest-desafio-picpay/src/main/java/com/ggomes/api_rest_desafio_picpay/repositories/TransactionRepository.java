package com.ggomes.api_rest_desafio_picpay.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ggomes.api_rest_desafio_picpay.entities.TransactionEntity;


@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByPayerId(Long payerId);
    Optional<TransactionEntity> findByPayeeId(Long payeeId);
}