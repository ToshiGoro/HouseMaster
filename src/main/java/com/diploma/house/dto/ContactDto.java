package com.diploma.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "DTO контакта")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {

    @Schema(description = "Идентификатор контакта")
    private UUID id;

    @Schema(description = "Идентификатор человека")
    private UUID personId;

    @Schema(description = "Тип контакта")
    private ContactTypeDto contactType;

    @Schema(description = "Контактная информация")
    private String contact;

}
