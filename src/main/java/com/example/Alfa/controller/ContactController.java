package com.example.Alfa.controller;

import com.example.Alfa.dto.ContactRequest;
import com.example.Alfa.dto.ContactResponse;
import com.example.Alfa.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")  // Рекомендую использовать множественное число для REST endpoints
@Tag(name = "Contact API", description = "Операции управления контактными данными")
@RequiredArgsConstructor
public class ContactController extends BaseController {
    private final ContactService contactService;

    @Operation(summary = "Получить контакт по ID", description = "Возвращает детальную информацию о контакте по его идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContact(@Parameter(description = "ID контакта", example = "1", required = true) @PathVariable Long id) {
        ContactResponse contact = contactService.getContact(id);
        return ResponseEntity.ok(contact);
    }

    @Operation(summary = "Создать новый контакт", description = "Создает новую запись контактных данных")
    @PostMapping
    public ResponseEntity<ContactResponse> createContact(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания контакта", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactRequest.class), examples = @ExampleObject(value = """
            {
              "phone": "89123456789",
              "email": "ivanov@example.com"
            }
            """))) @RequestBody ContactRequest request) {
        ContactResponse savedContact = contactService.saveContact(request);
        return ResponseEntity.ok(savedContact);
    }

    @Operation(summary = "Обновить контакт", description = "Полностью обновляет контактные данные по ID")
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(@Parameter(description = "ID контакта для обновления", example = "1", required = true) @PathVariable Long id,
                                                         @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Новые данные контакта", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactRequest.class), examples = @ExampleObject(value = """
                                                                 {
                                                                   "phone": "89998887766",
                                                                   "email": "updated@example.com"
                                                                 }
                                                                 """))) @RequestBody ContactRequest request) {
        ContactResponse updatedContact = contactService.updateContact(id, request);
        return ResponseEntity.ok(updatedContact);
    }

    @Operation(summary = "Удалить контакт", description = "Удаляет контактные данные по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@Parameter(description = "ID контакта для удаления", example = "1", required = true) @PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }
}