package com.example.Alfa.service.impl;

import com.example.Alfa.dto.ContactRequest;
import com.example.Alfa.dto.ContactResponse;
import com.example.Alfa.entity.Contact;
import com.example.Alfa.repository.ContactRepository;
import com.example.Alfa.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    @Override
    public ContactResponse getContact(Long id) {
        return toContactResponse(contactRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Contact not found")));
    }

    @Override
    public ContactResponse saveContact(ContactRequest request) {
        validateContactInfo(request);

        if (contactRepository.existsByPhoneOrEmail(request.getPhone(), request.getEmail())) {
            throw new IllegalArgumentException("Contact with this phone or email already exists");
        }

        return toContactResponse(contactRepository.save(toContact(request)));
    }

    @Override
    public ContactResponse updateContact(Long id, ContactRequest request) {
        validateContactInfo(request);

        Contact contactForUpdate = contactRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Contact not found"));

        if (!request.getPhone().equals(contactForUpdate.getPhone())) {
            if (contactRepository.existsByPhone(request.getPhone())) {
                throw new IllegalArgumentException("Phone already exists");
            }
            contactForUpdate.setPhone(request.getPhone());
        }

        if (!request.getEmail().equals(contactForUpdate.getEmail())) {
            if (contactRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            contactForUpdate.setEmail(request.getEmail());
        }

        return toContactResponse(contactRepository.save(contactForUpdate));
    }

    @Override
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Contact not found"));
        contactRepository.delete(contact);
    }

    private void validateContactInfo(ContactRequest request) {
        if (request.getPhone() == null || !Pattern.matches("^8\\d{10}$", request.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        if (request.getEmail() == null || !Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", request.getEmail())) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    private ContactResponse toContactResponse(Contact contact) {
        ContactResponse response = new ContactResponse();
        response.setPhone(contact.getPhone());
        response.setEmail(contact.getEmail());
        return response;
    }

    private Contact toContact(ContactRequest request) {
        Contact contact = new Contact();
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        return contact;
    }
}
