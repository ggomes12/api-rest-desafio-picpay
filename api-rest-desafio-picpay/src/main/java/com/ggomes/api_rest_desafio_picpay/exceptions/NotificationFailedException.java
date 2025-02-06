package com.ggomes.api_rest_desafio_picpay.exceptions;

public class NotificationFailedException extends RuntimeException {
    public NotificationFailedException(String message) {
        super(message);
    }
}
