package com.diploma.house.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FlatResponseDto {
    private UUID id;
    private Integer flatNumber;
    private UUID houseId;
    private List<PersonResponseDto> residents;
}