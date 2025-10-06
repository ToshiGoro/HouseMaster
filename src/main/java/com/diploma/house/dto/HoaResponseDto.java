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
public class HoaResponseDto {

    @Schema(description = "Идентификатор ТСЖ")
    private UUID id;

    @Schema(description = "Наименование ТСЖ")
    private String name;

    @Schema(description = "Дата создания ТСЖ")
    private LocalDateTime creationDate;

    @Schema(description = "Дата ликвидации ТСЖ")
    private LocalDateTime liquidationDate;

    @Schema(description = "Список домов, входящих в данное ТСЖ")
    private List<HouseInfoDto> houses;

    @Schema(description = "Количество домов, входящих в данное ТСЖ")
    private Integer housesCount;

    @Schema(description = "Время последней редакции записи")
    private LocalDateTime updatedAt;

    @Data
    public static class HouseInfoDto {
        private UUID id;
        private String address;
        private Double livingArea;
    }

}
