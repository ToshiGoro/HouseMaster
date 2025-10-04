package com.diploma.house.mapper;

import com.diploma.house.dto.HouseResponseDto;
import com.diploma.house.entity.House;
import org.springframework.stereotype.Component;

@Component
public class HouseMapper {

    public HouseResponseDto mapToHouseResponseDto(House house) {
        return new HouseResponseDto(
                house.getId(),
                house.getAddress(),
                house.getHoaId(),
                house.getLivingArea(),
                house.getNumOfFloors(),
                house.getNumOfSections(),
                house.getNumOfEntrances(),
                house.getNumOfFlats(),
                house.getNumOfOffices());
    }

}
