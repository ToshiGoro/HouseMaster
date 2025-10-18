package com.diploma.house.service;

import com.diploma.house.dto.ContactDto;
import com.diploma.house.entity.Contact;
import com.diploma.house.entity.ContactType;
import com.diploma.house.entity.Person;
import com.diploma.house.mapper.ContactMapper;
import com.diploma.house.repository.ContactRepository;
import com.diploma.house.repository.ContactTypeRepository;
import com.diploma.house.repository.PersonRepository;
import com.diploma.house.request.ContactRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactTypeRepository contactTypeRepository;
    private final PersonRepository personRepository;
    private final ContactMapper contactMapper;

    private static final String CONTACT_TYPE_NOT_FOUND = "Тип контакта с id %s не найден";
    private static final String PERSON_NOT_FOUND = "Человек с id %s не найден";
    private static final String CONTACT_NOT_FOUND = "Контакт с id %s не найден";

    public List<ContactDto> getContactsByPersonId(UUID personId) {

        List<Contact> contacts = contactRepository.findByPersonIdOrderByContactType(personId);

        return contacts.stream()
                .map(contactMapper::mapToContactDto)
                .collect(Collectors.toList());

    }

    @Transactional
    public ContactDto createContact(UUID personId, ContactRequest request) {

        validateContact(request.getContactTypeId(), request.getContact());

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(PERSON_NOT_FOUND, personId)));

        ContactType contactType = contactTypeRepository.findById(request.getContactTypeId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(CONTACT_TYPE_NOT_FOUND, request.getContactTypeId())));

        // Проверяем уникальность
        if (contactRepository.existsByPersonIdAndContactTypeIdAndContact(
                personId, request.getContactTypeId(), request.getContact())) {
            throw new IllegalStateException("Такой контакт уже существует для этого человека");
        }

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID());
        contact.setPerson(person);
        contact.setContactType(contactType);
        contact.setContact(request.getContact());

        Contact savedContact = contactRepository.save(contact);

        return contactMapper.mapToContactDto(savedContact);

    }

    @Transactional
    public ContactDto updateContact(UUID contactId, ContactRequest request) {

        validateContact(request.getContactTypeId(), request.getContact());

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(CONTACT_NOT_FOUND, contactId)));

        ContactType contactType = contactTypeRepository.findById(request.getContactTypeId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(CONTACT_TYPE_NOT_FOUND, request.getContactTypeId())));

        // Проверяем уникальность (исключая текущий контакт)
        if (contactRepository.existsByPersonIdAndContactTypeIdAndContact(
                contact.getPerson().getId(), request.getContactTypeId(), request.getContact()) &&
                !contact.getId().equals(contactId)) {
            throw new IllegalStateException("Такой контакт уже существует для этого человека");
        }

        contact.setContactType(contactType);
        contact.setContact(request.getContact());

        return contactMapper.mapToContactDto(contact);

    }

    @Transactional
    public void deleteContact(UUID contactId) {

        if (!contactRepository.existsById(contactId)) {
            throw new EntityNotFoundException(String.format(CONTACT_NOT_FOUND, contactId));
        }

        contactRepository.deleteById(contactId);

    }

    private void validateContact(UUID contactTypeId, String contact) {
        ContactType contactType = contactTypeRepository.findById(contactTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Тип контакта не найден"));

        switch (contactType.getContactType()) {

            case "E-mail":
                if (!isValidEmail(contact)) {
                    throw new IllegalArgumentException("Некорректный формат email");
                }
                break;

            case "Телефон":
                if (!isValidPhone(contact)) {
                    throw new IllegalArgumentException("Некорректный формат телефона");
                }
                break;

            case "Telegram":
                if (!isValidTelegram(contact)) {
                    throw new IllegalArgumentException("Некорректный формат Telegram");
                }
                break;

            case "WhatsApp":
                if (!isValidWhatsApp(contact)) {
                    throw new IllegalArgumentException("Некорректный формат WhatsApp");
                }
                break;

        }

    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^[\\+]?[0-9\\-\\(\\)\\s]{10,}$");
    }

    private boolean isValidTelegram(String phone) {
        return phone.matches("^[\\+]?[0-9\\-\\(\\)\\s]{10,}$");
    }

    private boolean isValidWhatsApp(String phone) {
        return phone.matches("^[\\+]?[0-9\\-\\(\\)\\s]{10,}$");
    }

}
