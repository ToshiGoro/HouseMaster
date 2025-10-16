package com.diploma.house.dto;

import lombok.Data;
import java.util.UUID;
import java.util.Set;

@Data
public class FlatResponseDto {
    private UUID id;
    private Integer flatNumber;
    private UUID houseId;
    private Set<UUID> residentIds;
}