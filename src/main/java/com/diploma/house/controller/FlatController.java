package com.diploma.house.controller;

import com.diploma.house.dto.FlatResponseDto;
import com.diploma.house.request.FlatRemoveResidentRequest;
import com.diploma.house.request.FlatRequest;
import com.diploma.house.request.FlatAddResidentRequest;
import com.diploma.house.service.FlatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flats")
@Validated
@RequiredArgsConstructor
public class FlatController {

    private final FlatService flatService;

    @GetMapping("/get/{id}")
    public ResponseEntity<FlatResponseDto> getFlat(@PathVariable UUID id) {
        FlatResponseDto flat = flatService.getFlat(id);
        return ResponseEntity.ok(flat);
    }

    @GetMapping("/get/by-house/{houseId}")
    public ResponseEntity<List<FlatResponseDto>> getFlatsByHouse(@PathVariable UUID houseId) {
        List<FlatResponseDto> flats = flatService.getFlatsByHouse(houseId);
        return ResponseEntity.ok(flats);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FlatResponseDto> updateFlat(
            @PathVariable UUID id,
            @Valid @RequestBody FlatRequest request) {
        FlatResponseDto updatedFlat = flatService.updateFlat(id, request);
        return ResponseEntity.ok(updatedFlat);
    }

    // Метод DELETE не создаём, так как удаление квартир возможно только при удалении дома, к которому они относятся.
    // Это уже настроено в скриптах миграции: FOREIGN KEY (flat_id) REFERENCES flats(id) ON DELETE CASCADE

    // Добавить жильца в квартиру
    @PostMapping("/residents")
    public ResponseEntity<String> addResident(@RequestBody FlatAddResidentRequest request) {
        try {
            flatService.addResidentToFlat(request.flatId(), request.personId());
            return ResponseEntity.ok("Жилец успешно добавлен в квартиру");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при добавлении жильца: " + e.getMessage());
        }
    }

    // Удалить жильца из квартиры
    @DeleteMapping("/residents")
    public ResponseEntity<String> removeResident(@RequestBody FlatRemoveResidentRequest request) {
        try {
            flatService.removeResidentFromFlat(request.flatId(), request.personId());
            return ResponseEntity.ok("Жилец успешно удален из квартиры");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при удалении жильца: " + e.getMessage());
        }
    }

}
