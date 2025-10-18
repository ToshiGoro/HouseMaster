package com.diploma.house.controller;

import com.diploma.house.dto.PositionDto;
import com.diploma.house.request.PositionRequest;
import com.diploma.house.dto.PositionTypeDto;
import com.diploma.house.entity.Position;
import com.diploma.house.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
@Tag(name = "Positions", description = "API для управления должностями сотрудников")
public class PositionController {

    private final PositionService positionService;

    @GetMapping
    @Operation(summary = "Получить все должности")
    public ResponseEntity<List<PositionDto>> getAllPositions() {

        List<Position> positions = positionService.getAllPositions();
        List<PositionDto> dtos = positions.stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(dtos);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить должность по ID")
    public ResponseEntity<PositionDto> getPositionById(@PathVariable UUID id) {

        Position position = positionService.getPositionById(id);
        if (position != null) {
            return ResponseEntity.ok(convertToDTO(position));
        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping("/house/{houseId}")
    @Operation(summary = "Получить все должности по дому")
    public ResponseEntity<List<PositionDto>> getPositionsByHouse(@PathVariable UUID houseId) {

        List<Position> positions = positionService.getPositionsByHouse(houseId);
        List<PositionDto> dtos = positions.stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(dtos);

    }

    @GetMapping("/person/{personId}")
    @Operation(summary = "Получить все должности сотрудника")
    public ResponseEntity<List<PositionDto>> getPositionsByPerson(@PathVariable UUID personId) {

        List<Position> positions = positionService.getPositionsByPerson(personId);
        List<PositionDto> dtos = positions.stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(dtos);

    }

    @PostMapping
    @Operation(summary = "Создать новую должность")
    public ResponseEntity<PositionDto> createPosition(@RequestBody PositionRequest request) {

        Position position = positionService.createPosition(
                request.getPersonId(),
                request.getPositionTypeId(),
                request.getHouseId()
        );

        if (position != null) {
            return ResponseEntity.ok(convertToDTO(position));
        }

        return ResponseEntity.badRequest().build();

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить должность")
    public ResponseEntity<Void> deletePosition(@PathVariable UUID id) {

        if (positionService.deletePosition(id)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();

    }

    private PositionDto convertToDTO(Position position) {

        PositionTypeDto positionTypeDTO = new PositionTypeDto(
                position.getPositionType().getId(),
                position.getPositionType().getPositionType()
        );

        return new PositionDto(
                position.getId(),
                position.getPerson().getId(),
                position.getPerson().getFullName(),
                positionTypeDTO,
                position.getHouse().getId(),
                position.getHouse().getAddress()
        );

    }

}
