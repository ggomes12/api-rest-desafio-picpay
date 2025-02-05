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

@RestController
@RequestMapping("/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@RequestBody WalletRequestDTO walletDTO) {
        return ResponseEntity.ok(walletService.createWallet(walletDTO));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponseDTO> getWalletByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getWalletByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<WalletResponseDTO>> getAllWallets() {
        return ResponseEntity.ok(walletService.getAllWallets());
    }
}
