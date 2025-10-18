package com.diploma.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "DTO должности сотрудника")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionDto {

    @Schema(description = "Идентификатор должности")
    private UUID id;

    @Schema(description = "Идентификатор сотрудника")
    private UUID personId;

    @Schema(description = "ФИО сотрудника")
    private String personName;

    @Schema(description = "Тип должности")
    private PositionTypeDto positionType;

    @Schema(description = "Идентификатор дома")
    private UUID houseId;

    @Schema(description = "Адрес дома")
    private String houseAddress;

}
