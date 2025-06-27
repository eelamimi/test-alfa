package com.example.Alfa.controller;

import com.example.Alfa.dto.ContactRequest;
import com.example.Alfa.dto.ContactResponse;
import com.example.Alfa.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ContactService contactService;

    @Test
    void getContact_ShouldReturnContact() throws Exception {
        ContactResponse response = new ContactResponse();
        response.setPhone("89123456789");
        response.setEmail("test@example.com");

        Mockito.when(contactService.getContact(1L)).thenReturn(response);

        mockMvc.perform(get("/api/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("89123456789"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void createContact_ShouldReturnCreatedContact() throws Exception {
        ContactRequest request = new ContactRequest("89123456789", "test@example.com");
        ContactResponse response = new ContactResponse();
        response.setPhone("89123456789");
        response.setEmail("test@example.com");

        Mockito.when(contactService.saveContact(Mockito.any(ContactRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("89123456789"));
    }

    @Test
    void createContact_ShouldReturnBadRequest_WhenInvalidPhone() throws Exception {
        ContactRequest request = new ContactRequest("123", "test@example.com");

        Mockito.when(contactService.saveContact(Mockito.any(ContactRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid phone number"));

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateContact_ShouldReturnUpdatedContact() throws Exception {
        ContactRequest request = new ContactRequest("89998887766", "updated@example.com");
        ContactResponse response = new ContactResponse();
        response.setPhone("89998887766");
        response.setEmail("updated@example.com");

        Mockito.when(contactService.updateContact(Mockito.eq(1L), Mockito.any(ContactRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("89998887766"));
    }

    @Test
    void updateContact_ShouldReturnBadRequest_WhenDuplicatePhone() throws Exception {
        ContactRequest request = new ContactRequest("89998887766", "updated@example.com");

        Mockito.when(contactService.updateContact(Mockito.eq(1L), Mockito.any(ContactRequest.class)))
                .thenThrow(new IllegalArgumentException("Phone already exists"));

        mockMvc.perform(put("/api/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteContact_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/contacts/1"))
                .andExpect(status().isOk());

        Mockito.verify(contactService).deleteContact(1L);
    }

    @Test
    void deleteContact_ShouldReturnNotFound_WhenContactNotExists() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Contact not found"))
                .when(contactService).deleteContact(1L);

        mockMvc.perform(delete("/api/contacts/1"))
                .andExpect(status().isNotFound());
    }
}