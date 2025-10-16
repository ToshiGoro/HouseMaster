package com.diploma.house.mapper;

import com.diploma.house.dto.FlatResponseDto;
import com.diploma.house.entity.Flat;
import com.diploma.house.entity.Person;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FlatMapper {

    public FlatResponseDto mapToFlatResponseDto(Flat flat) {
        FlatResponseDto dto = new FlatResponseDto();
        dto.setId(flat.getId());
        dto.setFlatNumber(flat.getFlatNumber());
        dto.setHouseId(flat.getHouse().getId());

        dto.setResidentIds(
                flat.getResidents().stream()
                        .map(Person::getId)
                        .collect(Collectors.toSet())
        );

        return dto;

    }

}
