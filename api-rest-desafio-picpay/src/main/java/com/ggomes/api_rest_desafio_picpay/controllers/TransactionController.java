package com.ggomes.api_rest_desafio_picpay.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggomes.api_rest_desafio_picpay.dtos.TransactionRequestDTO;
import com.ggomes.api_rest_desafio_picpay.dtos.TransactionResponseDTO;
import com.ggomes.api_rest_desafio_picpay.entities.TransactionEntity;
import com.ggomes.api_rest_desafio_picpay.services.TransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionRequestDTO transactionDTO) {
    	
        log.info("Processing transaction from payer ID: {} to payee ID: {}", transactionDTO.getPayerId(), transactionDTO.getPayeeId());
        TransactionResponseDTO response = transactionService.createTransaction(transactionDTO);
        
        log.info("Transaction completed successfully with ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
    	
        log.info("Fetching transaction with ID: {}", id);
        Optional<TransactionEntity> transaction = transactionService.findById(id);
        
        return transaction.map(t -> {
        	
            log.info("Transaction found with ID: {}", t.getId());
            return ResponseEntity.ok(transactionService.convertToDTO(t));
            
        }).orElseGet(() -> {
        	
            log.warn("Transaction not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        });
    }

    
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
    	
        log.info("Fetching all transactions...");
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactions();
        
        log.info("Total transactions retrieved: {}", transactions.size());
        return ResponseEntity.ok(transactions);
    }
}
