package com.diploma.house.controller;

import com.diploma.house.dto.PersonResponseDto;
import com.diploma.house.request.PersonRequest;
import com.diploma.house.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/persons")
@Validated
public class PersonController {

    @Autowired
    PersonService personService;

    @PostMapping("/create")
    public ResponseEntity<PersonResponseDto> createPerson(@Valid @RequestBody PersonRequest request) {

        PersonResponseDto customerResponseDto = personService.createPerson(request);

        return ResponseEntity.ok(customerResponseDto);

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PersonResponseDto> getPerson(@PathVariable UUID id) {

        PersonResponseDto customerResponseDto = personService.getPerson(id);

        return ResponseEntity.ok(customerResponseDto);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PersonResponseDto> updatePerson(@PathVariable UUID id,
                                                        @Valid @RequestBody PersonRequest request) {

        PersonResponseDto updatedPerson = personService.updatePerson(id, request);

        return ResponseEntity.ok(updatedPerson);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id) {

        personService.deletePerson(id);

        return ResponseEntity.noContent().build();

    }

}
