package com.ggomes.api_rest_desafio_picpay.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private String status;
    private Long payerId;
    private Long payeeId;
    private LocalDateTime createdAt;
}