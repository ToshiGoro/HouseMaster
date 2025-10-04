package com.diploma.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO с основной информацией для работы со списком всех людей")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponseDto {

    @Schema(description = "Идентификатор человека")
    private UUID id;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Отчество")
    private String secondName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Пол")
    private Boolean gender;

    @Schema(description = "Дата рождения")
    private LocalDateTime birthDate;

}
