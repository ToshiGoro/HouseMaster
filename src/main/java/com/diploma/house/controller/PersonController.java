package com.diploma.house.controller;

import com.diploma.house.dto.PersonResponseDto;
import com.diploma.house.dto.ResidentInfoDto;
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

    @Operation(summary = "Получить список жителей дома")
    @GetMapping("/by-house/{houseId}")
    public ResponseEntity<List<ResidentInfoDto>> getResidentsByHouse(@PathVariable UUID houseId) {

        try {

            List<ResidentInfoDto> residents = personService.getResidentsByHouseId(houseId);

            return ResponseEntity.ok(residents);

        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();

        }

    }

    @Operation(summary = "Проверить возможность удаления жителя")
    @GetMapping("/{personId}/can-delete")
    public ResponseEntity<Boolean> canDeletePerson(@PathVariable UUID personId) {

        try {

            boolean canDelete = personService.canDeletePerson(personId);

            return ResponseEntity.ok(canDelete);

        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();

        }

    }

    @Operation(summary = "Получить список квартир, где проживает человек")
    @GetMapping("/{personId}/residences")
    public ResponseEntity<List<ResidentInfoDto.FlatInfoDTO>> getPersonResidences(@PathVariable UUID personId) {

        try {

            List<ResidentInfoDto.FlatInfoDTO> residences = personService.getPersonResidences(personId);

            return ResponseEntity.ok(residences);

        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();

        }

    }

    @Operation(summary = "Удалить жителя (если он не проживает в квартирах)")
    @DeleteMapping("/{personId}")
    public ResponseEntity<?> deletePerson(@PathVariable UUID personId) {

        try {

            if (!personService.canDeletePerson(personId)) {

                return ResponseEntity.badRequest()
                        .body("Невозможно удалить: человек зарегистрирован в квартирах");

            }

            personService.deletePerson(personId);
            
            return ResponseEntity.ok().build();

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body("Ошибка при удалении записи о человеке: " + e.getMessage());

        }

    }

}
