package com.ggomes.api_rest_desafio_picpay.services;

import com.ggomes.api_rest_desafio_picpay.entities.TransactionEntity;
import com.ggomes.api_rest_desafio_picpay.entities.enums.TransactionStatus;
import com.ggomes.api_rest_desafio_picpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletService walletService;

    public TransactionEntity createTransaction(TransactionEntity transaction) {
        if (!walletService.hasSufficientBalance(transaction.getPayer().getId(), transaction.getAmount())) {
            throw new RuntimeException("Insufficient balance");
        }

        walletService.updateBalance(transaction.getPayer().getId(), transaction.getAmount(), false);
        walletService.updateBalance(transaction.getPayee().getId(), transaction.getAmount(), true);

        transaction.setStatus(TransactionStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }

    public Optional<TransactionEntity> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<TransactionEntity> findAll() {
        return transactionRepository.findAll();
    }
}
