package com.diploma.house.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "DTO запроса создания человека")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequest {

    @Schema(description = "Имя")
    @JsonProperty("firstName")
    @NotBlank
    private String firstName;

    @Schema(description = "Отчество")
    @JsonProperty("secondName")
    private String secondName;

    @Schema(description = "Фамилия")
    @JsonProperty("lastName")
    @NotBlank
    private String lastName;

    @Schema(description = "Пол")
    @JsonProperty("gender")
    private Boolean gender;

    @Schema(description = "Дата рождения")
    @JsonProperty("birthDate")
    private LocalDate birthDate;

}
