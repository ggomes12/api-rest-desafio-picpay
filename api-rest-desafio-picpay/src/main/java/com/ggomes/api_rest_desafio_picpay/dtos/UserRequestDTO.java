package com.ggomes.api_rest_desafio_picpay.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    private String fullName;
    private String cpfCnpj;
    private String email;
    private String password;
    private String type;
}
