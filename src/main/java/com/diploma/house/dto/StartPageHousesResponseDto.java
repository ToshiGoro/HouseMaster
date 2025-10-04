package com.diploma.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "DTO с основной информацией для работы со списком всех домов")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartPageHousesResponseDto {

    @Schema(description = "Идентификатор дома")
    private UUID id;

    @Schema(description = "Адрес дома")
    private String address;

    @Schema(description = "Идентификатор ТСЖ")
    private UUID hoaId;

}
