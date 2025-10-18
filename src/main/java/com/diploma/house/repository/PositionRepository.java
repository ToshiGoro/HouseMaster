package com.diploma.house.repository;

import com.diploma.house.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {

    List<Position> findByHouseId(UUID houseId);
    List<Position> findByPersonId(UUID personId);
    Position findByPersonIdAndHouseId(UUID personId, UUID houseId);
    boolean existsByPersonIdAndPositionTypeIdAndHouseId(UUID personId, UUID positionTypeId, UUID houseId);

    @Query("SELECT p FROM Position p WHERE p.house.id = :houseId")
    List<Position> findEmployeesByHouse(@Param("houseId") UUID houseId);

}
