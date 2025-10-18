package com.diploma.house.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO для добавления жителя в квартиру")
public record FlatAddResidentRequest(
        @Schema(description = "ID квартиры", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID flatId,

        @Schema(description = "ID человека", example = "550e8400-e29b-41d4-a716-446655450001")
        UUID personId
) {}