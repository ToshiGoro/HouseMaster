package com.diploma.house.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Schema(description = "DTO с информацией о жителе для страницы жителей дома")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResidentInfoDto {

    @Schema(description = "Идентификатор человека")
    private UUID id;

    @Schema(description = "ФИО")
    private String fullName;

    @Schema(description = "Пол")
    private Boolean gender;

    @Schema(description = "Возраст")
    private Integer age;

    @Schema(description = "Список квартир, где является собственником")
    private List<FlatInfoDTO> ownedFlats;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FlatInfoDTO {
        private UUID id;
        private Integer flatNumber;
        private String houseAddress;
        private UUID houseId;

    }

}
