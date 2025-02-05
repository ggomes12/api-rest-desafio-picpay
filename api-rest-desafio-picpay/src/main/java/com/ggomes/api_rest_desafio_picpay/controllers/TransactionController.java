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

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionRequestDTO transaction) {
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        Optional<TransactionEntity> transaction = transactionService.findById(id);
        return transaction.map(t -> ResponseEntity.ok(transactionService.convertToDTO(t)))
                          .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}
