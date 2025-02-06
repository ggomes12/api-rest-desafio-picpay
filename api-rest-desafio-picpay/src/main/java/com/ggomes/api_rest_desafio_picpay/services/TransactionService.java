package com.ggomes.api_rest_desafio_picpay.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggomes.api_rest_desafio_picpay.dtos.TransactionRequestDTO;
import com.ggomes.api_rest_desafio_picpay.dtos.TransactionResponseDTO;
import com.ggomes.api_rest_desafio_picpay.entities.TransactionEntity;
import com.ggomes.api_rest_desafio_picpay.entities.UserEntity;
import com.ggomes.api_rest_desafio_picpay.entities.enums.TransactionStatus;
import com.ggomes.api_rest_desafio_picpay.entities.enums.UserType;
import com.ggomes.api_rest_desafio_picpay.exceptions.InsufficientBalanceException;
import com.ggomes.api_rest_desafio_picpay.exceptions.MerchantCannotSendMoneyException;
import com.ggomes.api_rest_desafio_picpay.exceptions.NotificationFailedException;
import com.ggomes.api_rest_desafio_picpay.exceptions.UnauthorizedTransactionException;
import com.ggomes.api_rest_desafio_picpay.exceptions.UserNotFoundException;
import com.ggomes.api_rest_desafio_picpay.repositories.TransactionRepository;
import com.ggomes.api_rest_desafio_picpay.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthorizationService authorizationService;
    
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionDTO) {
        log.info("Processing transaction from payer ID: {} to payee ID: {}", 
                 transactionDTO.getPayerId(), transactionDTO.getPayeeId());

        
        UserEntity payer = userRepository.findById(transactionDTO.getPayerId())
                .orElseThrow(() -> {
                    log.error("Payer not found with ID: {}", transactionDTO.getPayerId());
                    return new UserNotFoundException("Payer not found with ID: " + transactionDTO.getPayerId());
                });


        UserEntity payee = userRepository.findById(transactionDTO.getPayeeId())
                .orElseThrow(() -> {
                    log.error("Payee not found with ID: {}", transactionDTO.getPayeeId());
                    return new UserNotFoundException("Payee not found with ID: " + transactionDTO.getPayeeId());
                });


        if (payer.getType() == UserType.MERCHANT) {
            log.warn("Merchant users cannot send money.");
            throw new MerchantCannotSendMoneyException("MERCHANT users cannot send money.");
        }


        if (!walletService.hasSufficientBalance(payer.getId(), transactionDTO.getAmount())) {
            log.error("Insufficient balance for payer ID: {}", payer.getId());
            throw new InsufficientBalanceException("Insufficient balance for transaction.");
        }

  
        log.info("Checking external authorization service...");
        if (!authorizationService.isTransactionAuthorized()) {
            log.warn("Transaction was not authorized by external service.");
            throw new UnauthorizedTransactionException("Transaction was not authorized by external service.");
        }


        log.info("Processing transaction...");
        walletService.updateBalance(payer.getId(), transactionDTO.getAmount(), false);
        walletService.updateBalance(payee.getId(), transactionDTO.getAmount(), true);


        TransactionEntity transaction = new TransactionEntity();
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus(TransactionStatus.COMPLETED);

        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction completed successfully with ID: {}", savedTransaction.getId());


        log.info("Sending notification...");
        boolean notificationSent = notificationService.sendNotification("Transaction completed successfully for Payee ID: " + payee.getId());
        
        if (!notificationSent) {
            log.warn("Failed to send notification for transaction ID: {}", savedTransaction.getId());
            throw new NotificationFailedException("Notification service failed.");
        }

        return convertToDTO(savedTransaction);
    }

    public List<TransactionResponseDTO> getAllTransactions() {
        log.info("Fetching all transactions...");
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TransactionEntity> findById(Long id) {
        log.info("Fetching transaction with ID: {}", id);
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
