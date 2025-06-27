package com.example.Alfa.service.impl;

import com.example.Alfa.dto.ClientRequest;
import com.example.Alfa.dto.ClientResponse;
import com.example.Alfa.entity.Client;
import com.example.Alfa.entity.Contact;
import com.example.Alfa.repository.ClientRepository;
import com.example.Alfa.repository.ContactRepository;
import com.example.Alfa.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository;

    @Override
    public List<ClientResponse> getClients() {
        return clientRepository.findAll().stream().map(this::toClientResponse).toList();
    }

    @Override
    public ClientResponse getClient(Long id) {
        return toClientResponse(clientRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found")));
    }

    @Override
    public ClientResponse saveClient(ClientRequest request) {
        if (request.getContactId() == null || request.getName().isEmpty() || request.getLastName().isEmpty()) {
            throw new IllegalArgumentException(
                    "ContactId, Name and LastName are required fields. Please provide all required fields."
            );
        }
        return toClientResponse(clientRepository.save(toClient(request)));
    }

    @Override
    public ClientResponse updateClient(Long id, ClientRequest request) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        Contact contact = contactRepository.findById(request.getContactId()).orElseThrow(() -> new IllegalArgumentException("Contact not found"));

        if (clientRepository.existsByContactIdAndIdNot(request.getContactId(), id)) {
            throw new IllegalStateException("Contact is already used by another client");
        }

        client.setName(request.getName());
        client.setLastName(request.getLastName());
        client.setContact(contact);

        return toClientResponse(clientRepository.save(client));
    }

    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        clientRepository.delete(client);
    }

    private ClientResponse toClientResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setName(client.getName());
        response.setLastName(client.getLastName());
        Map<String, String> contacts = new HashMap<>(2);
        contacts.put("phone", client.getContact().getPhone());
        contacts.put("email", client.getContact().getEmail());
        response.setContacts(contacts);
        return response;
    }

    private Client toClient(ClientRequest request) {
        Client client = new Client();
        client.setName(request.getName());
        client.setLastName(request.getLastName());
        client.setContact(
                contactRepository
                        .findById(request.getContactId())
                        .orElseThrow(() -> new IllegalArgumentException("Contact not found")));
        return client;
    }
}
