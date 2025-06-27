package com.example.Alfa.service;

import com.example.Alfa.dto.ContactRequest;
import com.example.Alfa.dto.ContactResponse;
import com.example.Alfa.entity.Contact;
import com.example.Alfa.repository.ContactRepository;
import com.example.Alfa.service.impl.ContactServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    private final String validPhone = "89123456789";
    private final String validEmail = "test@example.com";
    @Mock
    private ContactRepository contactRepository;
    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    void getContact_shouldReturnContact_whenExists() {
        Long id = 1L;
        Contact contact = new Contact();
        contact.setId(id);
        contact.setPhone(validPhone);
        contact.setEmail(validEmail);

        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        ContactResponse response = contactService.getContact(id);

        assertNotNull(response);
        assertEquals(validPhone, response.getPhone());
        assertEquals(validEmail, response.getEmail());
    }

    @Test
    void getContact_shouldThrowException_whenNotExists() {
        Long id = 1L;
        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> contactService.getContact(id));
    }

    @Test
    void saveContact_shouldSaveContact_whenValidData() {
        ContactRequest request = new ContactRequest();
        request.setPhone(validPhone);
        request.setEmail(validEmail);

        when(contactRepository.existsByPhoneOrEmail(validPhone, validEmail)).thenReturn(false);
        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> {
            Contact contact = invocation.getArgument(0);
            contact.setId(1L);
            return contact;
        });

        ContactResponse response = contactService.saveContact(request);

        assertNotNull(response);
        assertEquals(validPhone, response.getPhone());
        assertEquals(validEmail, response.getEmail());
        verify(contactRepository).save(any(Contact.class));
    }

    @Test
    void saveContact_shouldThrowException_whenPhoneOrEmailExists() {
        ContactRequest request = new ContactRequest();
        request.setPhone(validPhone);
        request.setEmail(validEmail);

        when(contactRepository.existsByPhoneOrEmail(validPhone, validEmail)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> contactService.saveContact(request));
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void saveContact_shouldThrowException_whenInvalidPhone() {
        ContactRequest request = new ContactRequest();
        request.setPhone("invalid-phone");
        request.setEmail(validEmail);

        assertThrows(IllegalArgumentException.class, () -> contactService.saveContact(request));
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void saveContact_shouldThrowException_whenInvalidEmail() {
        ContactRequest request = new ContactRequest();
        request.setPhone(validPhone);
        request.setEmail("invalid-email");

        assertThrows(IllegalArgumentException.class, () -> contactService.saveContact(request));
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void updateContact_shouldUpdateContact_whenValidData() {
        Long id = 1L;
        String newPhone = "89987654321";
        String newEmail = "new@example.com";

        ContactRequest request = new ContactRequest();
        request.setPhone(newPhone);
        request.setEmail(newEmail);

        Contact existingContact = new Contact();
        existingContact.setId(id);
        existingContact.setPhone(validPhone);
        existingContact.setEmail(validEmail);

        when(contactRepository.findById(id)).thenReturn(Optional.of(existingContact));
        when(contactRepository.existsByPhone(newPhone)).thenReturn(false);
        when(contactRepository.existsByEmail(newEmail)).thenReturn(false);
        when(contactRepository.save(any(Contact.class))).thenReturn(existingContact);

        ContactResponse response = contactService.updateContact(id, request);

        assertNotNull(response);
        assertEquals(newPhone, response.getPhone());
        assertEquals(newEmail, response.getEmail());
        verify(contactRepository).save(existingContact);
    }

    @Test
    void updateContact_shouldThrowException_whenContactNotFound() {
        Long id = 1L;
        ContactRequest request = new ContactRequest();
        request.setPhone(validPhone);
        request.setEmail(validEmail);

        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> contactService.updateContact(id, request));
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void updateContact_shouldThrowException_whenPhoneExists() {
        Long id = 1L;
        String existingPhone = "89987654321";

        ContactRequest request = new ContactRequest();
        request.setPhone(existingPhone);
        request.setEmail(validEmail);

        Contact existingContact = new Contact();
        existingContact.setId(id);
        existingContact.setPhone(validPhone);
        existingContact.setEmail(validEmail);

        when(contactRepository.findById(id)).thenReturn(Optional.of(existingContact));
        when(contactRepository.existsByPhone(existingPhone)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> contactService.updateContact(id, request));
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void updateContact_shouldThrowException_whenEmailExists() {
        Long id = 1L;
        String existingEmail = "existing@example.com";

        ContactRequest request = new ContactRequest();
        request.setPhone(validPhone);
        request.setEmail(existingEmail);

        Contact existingContact = new Contact();
        existingContact.setId(id);
        existingContact.setPhone(validPhone);
        existingContact.setEmail(validEmail);

        when(contactRepository.findById(id)).thenReturn(Optional.of(existingContact));
        when(contactRepository.existsByEmail(existingEmail)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> contactService.updateContact(id, request));
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void updateContact_shouldThrowException_whenEmailIsNull() {
        Long id = 1L;
        String newPhone = "89987654321";

        ContactRequest request = new ContactRequest();
        request.setPhone(newPhone);
        request.setEmail(null);

        assertThrows(IllegalArgumentException.class, () -> contactService.updateContact(id, request));
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void updateContact_shouldThrowException_whenPhoneIsNull() {
        Long id = 1L;
        String newEmail = "new@example.com";

        ContactRequest request = new ContactRequest();
        request.setPhone(null);
        request.setEmail(newEmail);

        assertThrows(IllegalArgumentException.class, () -> contactService.updateContact(id, request));
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void deleteContact_shouldDeleteContact_whenExists() {
        Long id = 1L;
        Contact contact = new Contact();
        contact.setId(id);

        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        contactService.deleteContact(id);

        verify(contactRepository).delete(contact);
    }

    @Test
    void deleteContact_shouldThrowException_whenNotExists() {
        Long id = 1L;
        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> contactService.deleteContact(id));
        verify(contactRepository, never()).delete(any(Contact.class));
    }

}
