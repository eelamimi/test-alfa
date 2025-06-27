package com.example.Alfa.service;

import com.example.Alfa.dto.ClientRequest;
import com.example.Alfa.dto.ClientResponse;
import com.example.Alfa.entity.Client;
import com.example.Alfa.entity.Contact;
import com.example.Alfa.repository.ClientRepository;
import com.example.Alfa.repository.ContactRepository;
import com.example.Alfa.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void getClients_ShouldReturnAllClients() {
        Client client1 = createTestClient(1L, "Иван", "Иванов", "89123456789", "ivan@example.com");
        Client client2 = createTestClient(2L, "Петр", "Петров", "89234567890", "petr@example.com");

        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));

        List<ClientResponse> responses = clientService.getClients();

        assertEquals(2, responses.size());
        assertEquals("Иван", responses.get(0).getName());
        assertEquals("petr@example.com", responses.get(1).getContacts().get("email"));
    }

    @Test
    void getClient_ShouldReturnClient_WhenExists() {
        Client client = createTestClient(1L, "Иван", "Иванов", "89123456789", "ivan@example.com");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientResponse response = clientService.getClient(1L);

        assertNotNull(response);
        assertEquals("Иван", response.getName());
        assertEquals("89123456789", response.getContacts().get("phone"));
    }

    @Test
    void getClient_ShouldThrow_WhenNotExists() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clientService.getClient(1L));
    }

    @Test
    void saveClient_ShouldSaveValidClient() {
        ClientRequest request = new ClientRequest("Иван", "Иванов", 1L);
        Contact contact = new Contact(1L, "89123456789", "ivan@example.com");
        Client client = new Client(null, "Иван", "Иванов", contact);
        Client savedClient = new Client(1L, "Иван", "Иванов", contact);

        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(clientRepository.save(client)).thenReturn(savedClient);

        ClientResponse response = clientService.saveClient(request);

        assertNotNull(response);
        assertEquals("Иван", response.getName());
        verify(clientRepository).save(client);
    }

    @Test
    void saveClient_ShouldThrow_WhenRequiredFieldsMissing() {
        ClientRequest request1 = new ClientRequest("", "Иванов", 1L);
        ClientRequest request2 = new ClientRequest("Иван", "", 1L);
        ClientRequest request3 = new ClientRequest("Иван", "Иванов", null);

        assertThrows(IllegalArgumentException.class, () -> clientService.saveClient(request1));
        assertThrows(IllegalArgumentException.class, () -> clientService.saveClient(request2));
        assertThrows(IllegalArgumentException.class, () -> clientService.saveClient(request3));
    }

    @Test
    void updateClient_ShouldUpdateClient() {
        Client existingClient = createTestClient(1L, "СтароеИмя", "СтараяФамилия", "89123456789", "old@example.com");
        ClientRequest request = new ClientRequest("НовоеИмя", "НоваяФамилия", 2L);
        Contact newContact = new Contact(2L, "89998887766", "new@example.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existingClient));
        when(contactRepository.findById(2L)).thenReturn(Optional.of(newContact));
        when(clientRepository.existsByContactIdAndIdNot(2L, 1L)).thenReturn(false);
        when(clientRepository.save(existingClient)).thenReturn(existingClient);

        ClientResponse response = clientService.updateClient(1L, request);

        assertEquals("НовоеИмя", response.getName());
        assertEquals("new@example.com", response.getContacts().get("email"));
    }

    @Test
    void updateClient_ShouldThrow_WhenContactUsedByOtherClient() {
        Client existingClient = createTestClient(1L, "Иван", "Иванов", "89123456789", "ivan@example.com");
        ClientRequest request = new ClientRequest("Иван", "Иванов", 2L);
        Contact newContact = new Contact(2L, "89998887766", "new@example.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existingClient));
        when(contactRepository.findById(2L)).thenReturn(Optional.of(newContact));
        when(clientRepository.existsByContactIdAndIdNot(2L, 1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> clientService.updateClient(1L, request));
    }

    @Test
    void deleteClient_ShouldDelete_WhenExists() {
        Client client = createTestClient(1L, "Иван", "Иванов", "89123456789", "ivan@example.com");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.deleteClient(1L);

        verify(clientRepository).delete(client);
    }

    @Test
    void deleteClient_ShouldThrow_WhenNotExists() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clientService.deleteClient(1L));
    }

    private Client createTestClient(Long id, String name, String lastName, String phone, String email) {
        Contact contact = new Contact();
        contact.setId(id);
        contact.setPhone(phone);
        contact.setEmail(email);

        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setLastName(lastName);
        client.setContact(contact);
        return client;
    }
}