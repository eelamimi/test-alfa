package com.example.Alfa.controller;

import com.example.Alfa.dto.ClientRequest;
import com.example.Alfa.dto.ClientResponse;
import com.example.Alfa.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client API", description = "Операции управления клиентами")
@RequiredArgsConstructor
public class ClientController extends BaseController {
    private final ClientService clientService;

    @Operation(summary = "Получить список всех клиентов", description = "Возвращает полный список клиентов с их контактными данными")
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getClients() {
        List<ClientResponse> clients = clientService.getClients();
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Получить клиента по ID", description = "Возвращает детальную информацию о клиенте по его идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@Parameter(description = "ID клиента", example = "1", required = true) @PathVariable Long id) {
        ClientResponse client = clientService.getClient(id);
        return ResponseEntity.ok(client);
    }

    @Operation(summary = "Создать нового клиента", description = "Создает нового клиента с указанными данными")
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания клиента", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientRequest.class), examples = @ExampleObject(value = "{\"name\": \"Иван\", \"lastName\": \"Иванов\", \"contactId\": 1}"))) @RequestBody ClientRequest request) {
        ClientResponse savedClient = clientService.saveClient(request);
        return ResponseEntity.ok(savedClient);
    }

    @Operation(summary = "Обновить данные клиента", description = "Полностью обновляет данные клиента по его ID")
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@Parameter(description = "ID клиента для обновления", example = "1", required = true) @PathVariable Long id,
                                                       @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Новые данные клиента", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientRequest.class), examples = @ExampleObject(value = "{\"name\": \"Петр\", \"lastName\": \"Петров\", \"contactId\": 2}"))) @RequestBody ClientRequest request) {
        ClientResponse updatedClient = clientService.updateClient(id, request);
        return ResponseEntity.ok(updatedClient);
    }

    @Operation(summary = "Удалить клиента", description = "Удаляет клиента по его ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@Parameter(description = "ID клиента для удаления", example = "1", required = true) @PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}