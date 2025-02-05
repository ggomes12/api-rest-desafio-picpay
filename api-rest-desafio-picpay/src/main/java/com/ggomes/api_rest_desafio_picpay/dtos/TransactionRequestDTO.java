package com.ggomes.api_rest_desafio_picpay.dtos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequestDTO {
    private Long payerId;
    private Long payeeId;
    private BigDecimal amount;
}