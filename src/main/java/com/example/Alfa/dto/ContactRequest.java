package com.example.Alfa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest {
    @Schema(description = "Номер телефона", example = "89123456789", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Schema(description = "Email адрес", example = "ivanov@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}
