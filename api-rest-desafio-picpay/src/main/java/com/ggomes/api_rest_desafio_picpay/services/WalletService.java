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

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public WalletResponseDTO createWallet(WalletRequestDTO walletDTO) {
        UserEntity user = userRepository.findById(walletDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        WalletEntity wallet = new WalletEntity();
        wallet.setUser(user);
        wallet.setBalance(walletDTO.getBalance());

        WalletEntity savedWallet = walletRepository.save(wallet);
        return convertToDTO(savedWallet);
    }

    
    public WalletResponseDTO getWalletByUserId(Long userId) {
        WalletEntity wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        return convertToDTO(wallet);
    }

    
    public List<WalletResponseDTO> getAllWallets() {
        return walletRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public boolean hasSufficientBalance(Long userId, BigDecimal amount) {
        return walletRepository.findByUserId(userId)
                .map(wallet -> wallet.getBalance().compareTo(amount) >= 0)
                .orElse(false);
    }

    public void updateBalance(Long userId, BigDecimal amount, boolean isCredit) {
        WalletEntity wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (isCredit) {
            wallet.setBalance(wallet.getBalance().add(amount));
        } else {
            if (wallet.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance().subtract(amount));
        }
        walletRepository.save(wallet);
    }

    private WalletResponseDTO convertToDTO(WalletEntity wallet) {
        WalletResponseDTO dto = new WalletResponseDTO();
        dto.setId(wallet.getId());
        dto.setBalance(wallet.getBalance());
        return dto;
    }
}
