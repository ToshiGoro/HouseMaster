package com.diploma.house.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "contact_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactType {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "contact_type", nullable = false, unique = true)
    private String contactType;
}