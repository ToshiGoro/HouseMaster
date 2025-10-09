package com.diploma.house.dto;

import com.diploma.house.entity.House;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Schema(description = "DTO с сокращённой информацией для представления стартового списка домов")
@Setter
@Getter
public class HouseForStartPageResponseDto {

    @Schema(description = "Идентификатор дома")
    private UUID id;

    @Schema(description = "Адрес дома")
    private String address;

    @Schema(description = "Идентификатор ТСЖ")
    private String hoaName;

    public HouseForStartPageResponseDto(House house) {
        this.id = house.getId();
        this.address = house.getAddress();
        this.hoaName = house.getHoa() != null ? house.getHoa().getName() : "";
    }

}
