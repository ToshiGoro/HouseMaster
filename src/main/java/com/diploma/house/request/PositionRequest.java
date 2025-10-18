package com.diploma.house.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "Запрос на создание/обновление должности")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionRequest {

    @Schema(description = "Идентификатор сотрудника", required = true)
    private UUID personId;

    @Schema(description = "Идентификатор типа должности", required = true)
    private UUID positionTypeId;

    @Schema(description = "Идентификатор дома", required = true)
    private UUID houseId;

}
