package com.diploma.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "DTO типа должности")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionTypeDto {

    @Schema(description = "Идентификатор типа должности")
    private UUID id;

    @Schema(description = "Название должности")
    private String positionType;
}