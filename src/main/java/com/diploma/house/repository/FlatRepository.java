package com.diploma.house.repository;

import com.diploma.house.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlatRepository extends JpaRepository<Flat, UUID> {

    List<Flat> findByHouseId(UUID houseId);

    // Удаление жильца из квартиры - НАТИВНЫЙ запрос
    @Modifying
    @Query(value = "DELETE FROM flat_person WHERE flat_id = :flatId AND person_id = :personId",
            nativeQuery = true)
    int deleteResidentFromFlat(@Param("flatId") UUID flatId, @Param("personId") UUID personId);

    // Добавление жильца в квартиру - НАТИВНЫЙ запрос
    @Modifying
    @Query(value = "INSERT INTO flat_person (id, flat_id, person_id) " +
            "VALUES (:#{T(java.util.UUID).randomUUID()}, :flatId, :personId)",
            nativeQuery = true)
    void addResidentToFlat(@Param("flatId") UUID flatId, @Param("personId") UUID personId);

    // Проверка существования связи - НАТИВНЫЙ запрос
    @Query(value = "SELECT COUNT(*) > 0 FROM flat_person WHERE flat_id = :flatId AND person_id = :personId",
            nativeQuery = true)
    boolean existsResidentInFlat(@Param("flatId") UUID flatId, @Param("personId") UUID personId);

    // Получение количества жильцов в квартире - НАТИВНЫЙ запрос
    @Query(value = "SELECT COUNT(*) FROM flat_person WHERE flat_id = :flatId",
            nativeQuery = true)
    long countResidentsInFlat(@Param("flatId") UUID flatId);
}