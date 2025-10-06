package com.diploma.house.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "DTO запроса создания ТСЖ")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaRequest {

    @Schema(description = "Наименование ТСЖ")
    @JsonProperty("name")
    @NotBlank(message = "Необходимо указать наименование ТСЖ!")
    private String name;

    @Schema(description = "Дата создания ТСЖ")
    @JsonProperty("creationDate")
    private LocalDateTime creationDate;

    @Schema(description = "Дата ликвидации ТСЖ")
    @JsonProperty("liquidationDate")
    private LocalDateTime liquidationDate;

}
