package com.example.Alfa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ContactResponse {
    @Schema(description = "Номер телефона", example = "89123456789")
    private String phone;

    @Schema(description = "Email адрес", example = "ivanov@example.com")
    private String email;
}
