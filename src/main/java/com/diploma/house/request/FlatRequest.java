package com.diploma.house.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class FlatRequest {
    @NotNull(message = "Номер квартиры обязателен")
    private Integer flatNumber;

    @NotNull(message = "ID дома обязателен")
    private UUID houseId;
}