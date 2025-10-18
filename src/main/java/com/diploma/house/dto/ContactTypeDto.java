package com.diploma.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "DTO типа контакта")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactTypeDto {

    @Schema(description = "Идентификатор типа контакта")
    private UUID id;

    @Schema(description = "Наименование типа контакта")
    private String contactType;

}
