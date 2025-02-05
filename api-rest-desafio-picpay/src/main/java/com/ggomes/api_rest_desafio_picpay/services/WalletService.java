package com.ggomes.api_rest_desafio_picpay.services;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggomes.api_rest_desafio_picpay.dtos.WalletRequestDTO;
import com.ggomes.api_rest_desafio_picpay.dtos.WalletResponseDTO;
import com.ggomes.api_rest_desafio_picpay.entities.UserEntity;
import com.ggomes.api_rest_desafio_picpay.entities.WalletEntity;
import com.ggomes.api_rest_desafio_picpay.repositories.UserRepository;
import com.ggomes.api_rest_desafio_picpay.repositories.WalletRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    
    public WalletResponseDTO createWallet(WalletRequestDTO walletDTO) {
    	
    	log.info("Creating a wallet for user ID: {}", walletDTO.getUserId());
    	
        UserEntity user = userRepository.findById(walletDTO.getUserId())
        		.orElseThrow(() -> {
                    log.error("User not found with ID: {}", walletDTO.getUserId());
                    return new RuntimeException("User not found");
                });

        WalletEntity wallet = new WalletEntity();
        wallet.setUser(user);
        wallet.setBalance(walletDTO.getBalance());

        WalletEntity savedWallet = walletRepository.save(wallet);
        
        log.info("Wallet created successfully for user ID: {}", user.getId());
        return convertToDTO(savedWallet);
    }

    
    public WalletResponseDTO getWalletByUserId(Long userId) {
        log.info("Fetching wallet for user ID: {}", userId);
        WalletEntity wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Wallet not found for user ID: {}", userId);
                    return new RuntimeException("Wallet not found");
                });
        return convertToDTO(wallet);
    }

    
    public List<WalletResponseDTO> getAllWallets() {
        log.info("Fetching all wallets...");
        List<WalletResponseDTO> wallets = walletRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        log.info("Total wallets found: {}", wallets.size());
        return wallets;
    }
    
    

    public boolean hasSufficientBalance(Long userId, BigDecimal amount) {
    	
        log.info("Checking balance for user ID: {}", userId);
        return walletRepository.findByUserId(userId)
                .map(wallet -> wallet.getBalance().compareTo(amount) >= 0)
                .orElse(false);
    }
    
    

    public void updateBalance(Long userId, BigDecimal amount, boolean isCredit) {
    	
        log.info("Updating balance for user ID: {}, Amount: {}, isCredit: {}", userId, amount, isCredit);

        WalletEntity wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Wallet not found for user ID: {}", userId);
                    return new RuntimeException("Wallet not found");
                });

        if (isCredit) {
            wallet.setBalance(wallet.getBalance().add(amount));
        } else {
            if (wallet.getBalance().compareTo(amount) < 0) {
                log.error("Insufficient funds for user ID: {}", userId);
                throw new RuntimeException("Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance().subtract(amount));
        }

        walletRepository.save(wallet);
        log.info("Balance updated successfully for user ID: {}", userId);
    }

    
    
    private WalletResponseDTO convertToDTO(WalletEntity wallet) {
        WalletResponseDTO dto = new WalletResponseDTO();
        dto.setId(wallet.getId());
        dto.setBalance(wallet.getBalance());
        return dto;
    }
}
