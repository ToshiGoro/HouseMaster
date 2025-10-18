package com.diploma.house.controller;

import com.diploma.house.dto.PersonResponseDto;
import com.diploma.house.request.PersonRequest;
import com.diploma.house.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/persons")
@RequiredArgsConstructor
@Validated
public class PersonController {

    private final PersonService personService;

    @Operation(summary = "Создать новую запись о человеке")
    @PostMapping("/create")
    public ResponseEntity<PersonResponseDto> createPerson(@Valid @RequestBody PersonRequest request) {

        PersonResponseDto customerResponseDto = personService.createPerson(request);

        return ResponseEntity.ok(customerResponseDto);

    }

    @Operation(summary = "Найти человека по id")
    @GetMapping("/get/{id}")
    public ResponseEntity<PersonResponseDto> getPerson(@PathVariable UUID id) {

        PersonResponseDto personResponseDto = personService.getPerson(id);

        return ResponseEntity.ok(personResponseDto);

    }

    @Operation(summary = "Получить список всех людей")
    @GetMapping("/all")
    public ResponseEntity<List<PersonResponseDto>> getAllPersons() {

        List<PersonResponseDto> dtos = personService.getAllPersons();

        return ResponseEntity.ok(dtos);

    }

    @Operation(summary = "Внести изменения в запись о человеке")
    @PutMapping("/update/{id}")
    public ResponseEntity<PersonResponseDto> updatePerson(@PathVariable UUID id,
                                                        @Valid @RequestBody PersonRequest request) {

        PersonResponseDto updatedPerson = personService.updatePerson(id, request);

        return ResponseEntity.ok(updatedPerson);

    }

    @Operation(summary = "Удалить сведения о человеке")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id) {

        personService.deletePerson(id);

        return ResponseEntity.noContent().build();

    }

}
