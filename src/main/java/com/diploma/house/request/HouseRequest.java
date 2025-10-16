package com.diploma.house.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "DTO запроса создания дома")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseRequest {

    @Schema(description = "Адрес дома")
    @JsonProperty("address")
    @NotBlank(message = "Адрес дома обязателен для заполнения")
    private String address;

    @Schema(description = "ID ТСЖ (опционально)")
    @JsonProperty("hoaId")
    private UUID hoaId;

    @Schema(description = "Общая площадь помещений дома")
    @JsonProperty("livingArea")
    private Double livingArea;

    @Schema(description = "Количество этажей в доме")
    @Positive(message = "Количество этажей должно быть положительным")
    @JsonProperty("numOfFloors")
    private Integer numOfFloors;

    @Schema(description = "Количество подъездов в доме")
    @Positive(message = "Количество подъездов должно быть положительным")
    @JsonProperty("numOfSections")
    private Integer numOfSections;

    @Schema(description = "Количество входных групп дома")
    @Positive(message = "Количество входных групп должно быть положительным")
    @JsonProperty("numOfEntrances")
    private Integer numOfEntrances;

    @Schema(description = "Количество квартир в доме")
    @JsonProperty("numOfFlats")
    private Integer numOfFlats;

    @Schema(description = "Количество нежилых помещений в доме")
    @PositiveOrZero(message = "Количество нежилых помещений не может быть отрицательным")
    @JsonProperty("numOfOffices")
    private Integer numOfOffices;

}
