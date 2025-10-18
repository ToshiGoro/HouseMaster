package com.diploma.house.mapper;

import com.diploma.house.dto.FlatResponseDto;
import com.diploma.house.dto.PersonResponseDto;
import com.diploma.house.entity.Flat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlatMapper {

    private final PersonMapper personMapper;

    public FlatMapper(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    public FlatResponseDto mapToFlatResponseDto(Flat flat) {
        FlatResponseDto dto = new FlatResponseDto();
        dto.setId(flat.getId());
        dto.setFlatNumber(flat.getFlatNumber());
        dto.setHouseId(flat.getHouse().getId());

        List<PersonResponseDto> residentDtos = flat.getResidents().stream()
                .map(personMapper::mapToPersonResponseDto)
                .collect(Collectors.toList());

        dto.setResidents(residentDtos);

        return dto;

    }

}
