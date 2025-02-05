package com.ggomes.api_rest_desafio_picpay.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggomes.api_rest_desafio_picpay.dtos.UserRequestDTO;
import com.ggomes.api_rest_desafio_picpay.dtos.UserResponseDTO;
import com.ggomes.api_rest_desafio_picpay.entities.UserEntity;
import com.ggomes.api_rest_desafio_picpay.entities.enums.UserType;
import com.ggomes.api_rest_desafio_picpay.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO save(UserRequestDTO userDTO) {
    	
    	log.info("Creating user: {}", userDTO);
    	
        UserEntity user = new UserEntity();
        user.setFullName(userDTO.getFullName());
        user.setCpfCnpj(userDTO.getCpfCnpj());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); 
        user.setType(UserType.valueOf(userDTO.getType())); 

        UserEntity savedUser = userRepository.save(user);
        
        log.info("User created: {}", savedUser);
        return convertToResponseDTO(savedUser);
    }

    
    public List<UserResponseDTO> findAll() {
    	
        log.info("Fetching all users...");
        List<UserResponseDTO> users = userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        
        log.info("Total users found: {}", users.size());
        return users;
    }
    
    
    public Optional<UserResponseDTO> findById(Long id) {
    	
    	log.info("Fetching user with ID: {}", id);
    	return userRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    private UserResponseDTO convertToResponseDTO(UserEntity user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setCpfCnpj(user.getCpfCnpj());
        dto.setEmail(user.getEmail());
        dto.setType(user.getType().name());
        return dto;
    }
}


