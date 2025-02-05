package com.ggomes.api_rest_desafio_picpay.controllers;

import com.ggomes.api_rest_desafio_picpay.entities.WalletEntity;
import com.ggomes.api_rest_desafio_picpay.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletEntity> createWallet(@RequestBody WalletEntity wallet) {
        return ResponseEntity.ok(walletService.save(wallet));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long userId) {
        Optional<WalletEntity> wallet = walletService.findByUserId(userId);
        return wallet.map(w -> ResponseEntity.ok(w.getBalance())).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<WalletEntity>> getAllWallets() {
        return ResponseEntity.ok(walletService.findAll());
    }
}
