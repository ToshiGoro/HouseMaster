package com.diploma.house.mapper;

import com.diploma.house.dto.HoaResponseDto;
import com.diploma.house.dto.PersonResponseDto;
import com.diploma.house.entity.Hoa;
import com.diploma.house.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class HoaMapper {

    public HoaResponseDto mapToHoaResponseDto(Hoa hoa) {

        return new HoaResponseDto(
                hoa.getId(),
                hoa.getName(),
                hoa.getCreationDate(),
                hoa.getLiquidationDate());

    }

}
