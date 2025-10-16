package com.diploma.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "DTO с основной информацией для работы со списком всех людей")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaForHouseDisplayResponseDto {

    @Schema(description = "Идентификатор ТСЖ")
    private UUID id;

    @Schema(description = "Наименование ТСЖ")
    private String name;

}
