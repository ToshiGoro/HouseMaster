package com.diploma.house.controller;

import com.diploma.house.dto.PositionTypeDto;
import com.diploma.house.entity.PositionType;
import com.diploma.house.repository.PositionTypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/position-types")
@RequiredArgsConstructor
public class PositionTypeController {

    private final PositionTypeRepository positionTypeRepository;

    @Operation(summary = "Получить все виды должностей")
    @GetMapping
    public ResponseEntity<List<PositionTypeDto>> getAllPositionTypes() {

        List<PositionType> positionTypes = positionTypeRepository.findAll();
        List<PositionTypeDto> dtos = positionTypes.stream()
                .map(ct -> new PositionTypeDto(ct.getId(), ct.getPositionType()))
                .toList();

        return ResponseEntity.ok(dtos);

    }

}
