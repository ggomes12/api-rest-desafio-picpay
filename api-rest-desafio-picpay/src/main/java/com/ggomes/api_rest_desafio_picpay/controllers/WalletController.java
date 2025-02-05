package com.ggomes.api_rest_desafio_picpay.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggomes.api_rest_desafio_picpay.dtos.WalletRequestDTO;
import com.ggomes.api_rest_desafio_picpay.dtos.WalletResponseDTO;
import com.ggomes.api_rest_desafio_picpay.services.WalletService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/wallets")
@Slf4j
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@RequestBody WalletRequestDTO walletDTO) {
    	
        log.info("Received request to create wallet for user ID: {}", walletDTO.getUserId());
        WalletResponseDTO response = walletService.createWallet(walletDTO);
        
        log.info("Wallet created successfully for user ID: {}", walletDTO.getUserId());
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponseDTO> getWalletByUserId(@PathVariable Long userId) {
    	
        log.info("Fetching wallet for user ID: {}", userId);
        WalletResponseDTO response = walletService.getWalletByUserId(userId);
        
        log.info("Wallet retrieved for user ID: {}", userId);
        return ResponseEntity.ok(response);
    }

    
    @GetMapping
    public ResponseEntity<List<WalletResponseDTO>> getAllWallets() {
    	
        log.info("Fetching all wallets...");
        List<WalletResponseDTO> wallets = walletService.getAllWallets();
        
        log.info("Total wallets retrieved: {}", wallets.size());
        return ResponseEntity.ok(wallets);
    }
}
