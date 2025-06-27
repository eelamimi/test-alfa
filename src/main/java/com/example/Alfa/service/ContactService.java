package com.example.Alfa.service;

import com.example.Alfa.dto.ContactRequest;
import com.example.Alfa.dto.ContactResponse;
import org.springframework.stereotype.Service;

@Service
public interface ContactService {
    ContactResponse saveContact(ContactRequest request);

    ContactResponse getContact(Long id);

    ContactResponse updateContact(Long id, ContactRequest request);

    void deleteContact(Long id);
}
