package com.example.Alfa.controller;

import com.example.Alfa.dto.ClientRequest;
import com.example.Alfa.dto.ClientResponse;
import com.example.Alfa.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    @Test
    void getClients_ShouldReturnAllClients() throws Exception {
        ClientResponse response = createTestClientResponse();
        Mockito.when(clientService.getClients()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Иван"))
                .andExpect(jsonPath("$[0].contacts.phone").value("89123456789"));
    }

    @Test
    void getClient_ShouldReturnClientById() throws Exception {
        ClientResponse response = createTestClientResponse();
        Mockito.when(clientService.getClient(1L)).thenReturn(response);

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Иванов"))
                .andExpect(jsonPath("$.contacts.email").value("ivan@example.com"));
    }

    @Test
    void getClient_ShouldReturnNotFound_WhenClientNotExists() throws Exception {
        Mockito.when(clientService.getClient(1L))
                .thenThrow(new IllegalArgumentException("Client not found"));

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createClient_ShouldReturnCreatedClient() throws Exception {
        ClientRequest request = new ClientRequest("Иван", "Иванов", 1L);
        ClientResponse response = createTestClientResponse();

        Mockito.when(clientService.saveClient(Mockito.any(ClientRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Иван"));
    }

    @Test
    void createClient_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        ClientRequest request = new ClientRequest("", "", null);

        Mockito.when(clientService.saveClient(Mockito.any(ClientRequest.class)))
                .thenThrow(new IllegalArgumentException("ContactId, Name and LastName are required fields. Please provide all required fields."));

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateClient_ShouldReturnUpdatedClient() throws Exception {
        ClientRequest request = new ClientRequest("Петр", "Петров", 2L);
        ClientResponse response = new ClientResponse();
        response.setName("Петр");
        response.setLastName("Петров");
        Map<String, String> contacts = new HashMap<>();
        contacts.put("phone", "89998887766");
        contacts.put("email", "petr@example.com");
        response.setContacts(contacts);

        Mockito.when(clientService.updateClient(Mockito.eq(1L), Mockito.any(ClientRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Петров"))
                .andExpect(jsonPath("$.contacts.phone").value("89998887766"));
    }

    @Test
    void updateClient_ShouldReturnConflict_WhenContactUsed() throws Exception {
        ClientRequest request = new ClientRequest("Петр", "Петров", 2L);

        Mockito.when(clientService.updateClient(Mockito.eq(1L), Mockito.any(ClientRequest.class)))
                .thenThrow(new IllegalStateException("Contact is already used by another client"));

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteClient_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isOk());

        Mockito.verify(clientService).deleteClient(1L);
    }

    private ClientResponse createTestClientResponse() {
        ClientResponse response = new ClientResponse();
        response.setName("Иван");
        response.setLastName("Иванов");
        Map<String, String> contacts = new HashMap<>(2);
        contacts.put("phone", "89123456789");
        contacts.put("email", "ivan@example.com");
        response.setContacts(contacts);
        return response;
    }
}