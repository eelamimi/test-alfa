package com.example.Alfa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientRequest {
    @Schema(description = "Имя клиента", example = "Иван", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Фамилия клиента", example = "Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Schema(description = "ID контакта клиента", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long contactId;
}
