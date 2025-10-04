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
public class HouseResponseDto {

    @Schema(description = "Идентификатор дома")
    private UUID id;

    @Schema(description = "Адрес дома")
    private String address;

    @Schema(description = "Идентификатор ТСЖ")
    private UUID hoaId;

    @Schema(description = "Общая площадь помещений дома")
    private double livingArea;

    @Schema(description = "Количество этажей в доме")
    private int numOfFloors;

    @Schema(description = "Количество подъездов в доме")
    private int numOfSections;

    @Schema(description = "Количество входных групп дома")
    private int numOfEntrances;

    @Schema(description = "Количество квартир в доме")
    private int numOfFlats;

    @Schema(description = "Количество нежилых помещений в доме")
    private int numOfOffices;

}
