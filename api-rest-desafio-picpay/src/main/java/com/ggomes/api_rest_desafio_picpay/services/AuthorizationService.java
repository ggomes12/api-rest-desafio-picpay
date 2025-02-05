package com.ggomes.api_rest_desafio_picpay.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class AuthorizationService {

    private static final String AUTHORIZATION_API_URL = "https://util.devi.tools/api/v2/authorize";
    
    private final RestTemplate restTemplate = new RestTemplate();
    

    public boolean isTransactionAuthorized() {
    	
        log.info("Checking transaction authorization...");
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(AUTHORIZATION_API_URL, String.class);
            
            log.info("Authorization service response: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody().contains("true")) {
                log.info("Transaction authorized.");
                return true;
            } 
            else if (response.getStatusCode().is4xxClientError()) {
                log.warn("Transaction denied by external service.");
                return false;
            } 
            else {
                log.warn("Unexpected response from authorization service: {}", response.getBody());
                return false; 
            }
        } catch (Exception e) {
            log.error("Authorization service failed: {}", e.getMessage());
            return false;
        }
    }
}
