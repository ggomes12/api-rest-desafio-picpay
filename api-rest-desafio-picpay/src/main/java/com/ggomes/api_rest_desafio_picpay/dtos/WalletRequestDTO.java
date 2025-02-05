package com.ggomes.api_rest_desafio_picpay.dtos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WalletRequestDTO {
    private Long userId;
    private BigDecimal balance;
}