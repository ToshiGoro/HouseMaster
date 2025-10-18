package com.diploma.house.controller;

import com.diploma.house.dto.ContactDto;
import com.diploma.house.request.ContactRequest;
import com.diploma.house.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Validated
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "Получить контакты человека")
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<ContactDto>> getContactsByPerson(@PathVariable UUID personId) {

        List<ContactDto> contacts = contactService.getContactsByPersonId(personId);

        return ResponseEntity.ok(contacts);

    }

    @Operation(summary = "Создать контакт для человека")
    @PostMapping("/person/{personId}")
    public ResponseEntity<ContactDto> createContact(
            @PathVariable UUID personId,
            @Valid @RequestBody ContactRequest request) {

        ContactDto contact = contactService.createContact(personId, request);

        return ResponseEntity.ok(contact);

    }

    @Operation(summary = "Обновить контакт")
    @PutMapping("/{contactId}")
    public ResponseEntity<ContactDto> updateContact(
            @PathVariable UUID contactId,
            @Valid @RequestBody ContactRequest request) {

        ContactDto contact = contactService.updateContact(contactId, request);

        return ResponseEntity.ok(contact);

    }

    @Operation(summary = "Удалить контакт")
    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable UUID contactId) {

        contactService.deleteContact(contactId);

        return ResponseEntity.noContent().build();

    }

}
