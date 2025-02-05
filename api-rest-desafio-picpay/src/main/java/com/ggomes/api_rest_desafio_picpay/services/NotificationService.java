package com.ggomes.api_rest_desafio_picpay.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

    private static final String NOTIFICATION_API_URL = "https://util.devi.tools/api/v1/notify";

    public boolean sendNotification(String message) {
        log.info("Sending notification: {}", message);
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("message", message);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(NOTIFICATION_API_URL, request, String.class);

            boolean success = response.getStatusCode().is2xxSuccessful();
            if (success) {
                log.info("Notification sent successfully.");
            } else {
                log.warn("Failed to send notification.");
            }

            return success;
        } catch (Exception e) {
            log.error("Error while sending notification: {}", e.getMessage());
            return false;
        }
    }
}
