package com.diploma.house.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO запроса создания дома")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseRequest {

    @Schema(description = "Адрес дома")
    @JsonProperty("address")
    @NotBlank
    private String address;

    @Schema(description = "Идентификатор ТСЖ")
    @JsonProperty("hoaId")
    private UUID hoaId;

    @Schema(description = "Общая площадь помещений дома")
    @JsonProperty("livingArea")
    private double livingArea;

    @Schema(description = "Количество этажей в доме")
    @JsonProperty("numOfFloors")
    private int numOfFloors;

    @Schema(description = "Количество подъездов в доме")
    @JsonProperty("numOfSections")
    private int numOfSections;

    @Schema(description = "Количество входных групп дома")
    @JsonProperty("numOfEntrances")
    private int numOfEntrances;

    @Schema(description = "Количество квартир в доме")
    @JsonProperty("numOfFlats")
    private int numOfFlats;

    @Schema(description = "Количество нежилых помещений в доме")
    @JsonProperty("numOfOffices")
    private int numOfOffices;

}
