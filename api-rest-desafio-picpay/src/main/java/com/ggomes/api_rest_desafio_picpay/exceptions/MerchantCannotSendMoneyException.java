package com.ggomes.api_rest_desafio_picpay.exceptions;

public class MerchantCannotSendMoneyException extends RuntimeException {
    public MerchantCannotSendMoneyException(String message) {
        super(message);
    }
}
