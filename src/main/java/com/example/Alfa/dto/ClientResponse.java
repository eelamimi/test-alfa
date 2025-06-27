package com.example.Alfa.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
public class ClientResponse {
    @Schema(description = "Имя клиента", example = "Иван")
    private String name;

    @Schema(description = "Фамилия клиента", example = "Иванов")
    private String lastName;

    @ArraySchema(arraySchema = @Schema(
            description = "Контакты клиента",
            example = """
                    {
                        "phone": "89123456789",
                        "email": "ivanov@example.com"
                    }
                    """))
    private Map<String, String> contacts;
}
