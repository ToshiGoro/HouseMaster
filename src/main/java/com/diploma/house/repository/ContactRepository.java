package com.diploma.house.repository;

import com.diploma.house.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {

    List<Contact> findByPersonId(UUID personId);

    @Query("SELECT c FROM Contact c WHERE c.person.id = :personId ORDER BY c.contactType.contactType")
    List<Contact> findByPersonIdOrderByContactType(@Param("personId") UUID personId);

    boolean existsByPersonIdAndContactTypeIdAndContact(UUID personId, UUID contactTypeId, String contact);
}