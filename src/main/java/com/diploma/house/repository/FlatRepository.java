package com.diploma.house.repository;

import com.diploma.house.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlatRepository extends JpaRepository<Flat, UUID> {
    List<Flat> findByHouseId(UUID houseId);
}
