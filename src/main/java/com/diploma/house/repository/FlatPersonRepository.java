package com.diploma.house.repository;

import com.diploma.house.entity.FlatPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlatPersonRepository extends JpaRepository<FlatPerson, UUID> {

    List<FlatPerson> findByFlatId(UUID id);

}
