package com.ggomes.api_rest_desafio_picpay.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggomes.api_rest_desafio_picpay.dtos.TransactionRequestDTO;
import com.ggomes.api_rest_desafio_picpay.dtos.TransactionResponseDTO;
import com.ggomes.api_rest_desafio_picpay.entities.TransactionEntity;
import com.ggomes.api_rest_desafio_picpay.entities.UserEntity;
import com.ggomes.api_rest_desafio_picpay.entities.enums.TransactionStatus;
import com.ggomes.api_rest_desafio_picpay.repositories.TransactionRepository;
import com.ggomes.api_rest_desafio_picpay.repositories.UserRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;


    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionDTO) {
        UserEntity payer = userRepository.findById(transactionDTO.getPayerId())
                .orElseThrow(() -> new RuntimeException("Payer not found"));

        UserEntity payee = userRepository.findById(transactionDTO.getPayeeId())
                .orElseThrow(() -> new RuntimeException("Payee not found"));


        if (!walletService.hasSufficientBalance(payer.getId(), transactionDTO.getAmount())) {
            throw new RuntimeException("Insufficient balance for transaction");
        }

        walletService.updateBalance(payer.getId(), transactionDTO.getAmount(), false);
        walletService.updateBalance(payee.getId(), transactionDTO.getAmount(), true);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus(TransactionStatus.COMPLETED);

        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }
    

    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    
    public Optional<TransactionEntity> findById(Long id) {
        return transactionRepository.findById(id);
    }


    public TransactionResponseDTO convertToDTO(TransactionEntity transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setStatus(transaction.getStatus().name());
        dto.setPayerId(transaction.getPayer().getId());
        dto.setPayeeId(transaction.getPayee().getId());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }
}
