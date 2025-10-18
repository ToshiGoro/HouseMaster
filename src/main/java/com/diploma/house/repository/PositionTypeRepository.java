package com.diploma.house.repository;

import com.diploma.house.entity.PositionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PositionTypeRepository extends JpaRepository<PositionType, UUID> {

    PositionType findByPositionType(String positionType);
    boolean existsByPositionType(String positionType);

}
