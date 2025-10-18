package com.diploma.house.controller;

import com.diploma.house.dto.ContactTypeDto;
import com.diploma.house.entity.ContactType;
import com.diploma.house.repository.ContactTypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contact-types")
@RequiredArgsConstructor
public class ContactTypeController {

    private final ContactTypeRepository contactTypeRepository;

    @Operation(summary = "Получить все типы контактов")
    @GetMapping
    public ResponseEntity<List<ContactTypeDto>> getAllContactTypes() {

        List<ContactType> contactTypes = contactTypeRepository.findAll();
        List<ContactTypeDto> dtos = contactTypes.stream()
                .map(ct -> new ContactTypeDto(ct.getId(), ct.getContactType()))
                .toList();

        return ResponseEntity.ok(dtos);

    }

}
