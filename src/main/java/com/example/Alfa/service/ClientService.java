package com.example.Alfa.service;

import com.example.Alfa.dto.ClientRequest;
import com.example.Alfa.dto.ClientResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientService {
    List<ClientResponse> getClients();

    ClientResponse getClient(Long id);

    ClientResponse saveClient(ClientRequest request);

    ClientResponse updateClient(Long id, ClientRequest request);

    void deleteClient(Long id);
}
