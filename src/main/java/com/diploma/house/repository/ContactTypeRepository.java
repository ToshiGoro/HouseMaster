package com.diploma.house.repository;

import com.diploma.house.entity.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactTypeRepository extends JpaRepository<ContactType, UUID> {

    ContactType findByContactType(String contactType);
}