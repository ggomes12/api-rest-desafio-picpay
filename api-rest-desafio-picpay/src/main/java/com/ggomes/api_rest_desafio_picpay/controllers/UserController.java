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

import com.ggomes.api_rest_desafio_picpay.dtos.UserRequestDTO;
import com.ggomes.api_rest_desafio_picpay.dtos.UserResponseDTO;
import com.ggomes.api_rest_desafio_picpay.services.UserService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userDTO) {
    	
    	log.info("Creating user {}", userDTO);
    	UserResponseDTO response = userService.save(userDTO);
    	
    	log.info("User created {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    	
    	log.info("Fetching all users...");
        List<UserResponseDTO> users = userService.findAll();
        
        log.info("Total users retrieved: {}", users.size());
        return ResponseEntity.ok(users);
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}