package com.ggomes.api_rest_desafio_picpay.services;

import com.ggomes.api_rest_desafio_picpay.entities.WalletEntity;
import com.ggomes.api_rest_desafio_picpay.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletService {
	
    @Autowired
    private WalletRepository walletRepository;

    
    public Optional<WalletEntity> findByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    
    
    public WalletEntity save(WalletEntity wallet) {
        return walletRepository.save(wallet);
    }

    
    
    public boolean hasSufficientBalance(Long userId, BigDecimal amount) {
    	
        return walletRepository.findByUserId(userId)
                .map(wallet -> new BigDecimal(wallet.getBalance()).compareTo(amount) >= 0)
                .orElse(false);
    }

    public void deductBalance(Long userId, BigDecimal amount) {
    	
        walletRepository.findByUserId(userId).ifPresent(wallet -> {
            BigDecimal currentBalance = new BigDecimal(wallet.getBalance());
            wallet.setBalance(currentBalance.subtract(amount).doubleValue());
            walletRepository.save(wallet);
        });
    }

    
    
    public void addBalance(Long userId, BigDecimal amount) {
    	
        walletRepository.findByUserId(userId).ifPresent(wallet -> {
            BigDecimal currentBalance = new BigDecimal(wallet.getBalance());
            wallet.setBalance(currentBalance.add(amount).doubleValue());
            walletRepository.save(wallet);
        });
    }
    
    
}
