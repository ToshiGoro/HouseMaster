package com.diploma.house.mapper;

import com.diploma.house.dto.HoaResponseDto;
import com.diploma.house.entity.Hoa;
import com.diploma.house.entity.House;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HoaMapper {

    public HoaResponseDto mapToHoaResponseDto(Hoa hoa) {
        HoaResponseDto dto = new HoaResponseDto();
        dto.setId(hoa.getId());
        dto.setName(hoa.getName());
        dto.setCreationDate(hoa.getCreationDate());
        dto.setLiquidationDate(hoa.getLiquidationDate());
        dto.setUpdatedAt(hoa.getUpdatedAt());

        // Маппинг связанных домов - ИСПРАВЛЕННАЯ ЧАСТЬ
        if (hoa.getHouses() != null && !hoa.getHouses().isEmpty()) {
            List<HoaResponseDto.HouseInfoDto> houseDtos = hoa.getHouses().stream()
                    .map(this::mapToHouseInfoDto)
                    .collect(Collectors.toList());
            dto.setHouses(houseDtos);
            dto.setHousesCount(houseDtos.size());
        } else {
            dto.setHouses(Collections.emptyList());
            dto.setHousesCount(0);
        }

        return dto;
    }

    // Вспомогательный метод для маппинга дома в упрощенный DTO
    private HoaResponseDto.HouseInfoDto mapToHouseInfoDto(House house) {
        HoaResponseDto.HouseInfoDto dto = new HoaResponseDto.HouseInfoDto();
        dto.setId(house.getId());
        dto.setAddress(house.getAddress());
        dto.setLivingArea(house.getLivingArea());
        return dto;
    }
}