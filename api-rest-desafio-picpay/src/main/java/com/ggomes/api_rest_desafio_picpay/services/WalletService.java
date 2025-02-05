package com.ggomes.api_rest_desafio_picpay.services;

import com.ggomes.api_rest_desafio_picpay.entities.WalletEntity;
import com.ggomes.api_rest_desafio_picpay.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    public WalletEntity save(WalletEntity wallet) {
        return walletRepository.save(wallet);
    }

    public Optional<WalletEntity> findByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    public List<WalletEntity> findAll() {
        return walletRepository.findAll();
    }

    public boolean hasSufficientBalance(Long userId, BigDecimal amount) {
        return walletRepository.findByUserId(userId)
                .map(wallet -> wallet.getBalance().compareTo(amount) >= 0)
                .orElse(false);
    }

    public void updateBalance(Long userId, BigDecimal amount, boolean isCredit) {
        walletRepository.findByUserId(userId).ifPresent(wallet -> {
            if (isCredit) {
                wallet.setBalance(wallet.getBalance().add(amount));
            } else {
                wallet.setBalance(wallet.getBalance().subtract(amount));
            }
            walletRepository.save(wallet);
        });
    }
}
