package com.diploma.house.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "DTO запроса создания/обновления контакта")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {

    @Schema(description = "ID типа контакта", required = true)
    @NotNull(message = "Тип контакта обязателен")
    private UUID contactTypeId;

    @Schema(description = "Контактная информация", required = true)
    @NotBlank(message = "Контактная информация обязательна")
    private String contact;

}
