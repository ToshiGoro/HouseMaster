package com.diploma.house.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "persons")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Person {

    @Id
    @Column
    private UUID id;

    @Column
    @NotBlank
    private String firstName;

    @Column
    private String secondName;

    @Column
    @NotBlank
    private String lastName;

    @Column
    private Boolean gender;

    @Column
    private LocalDateTime birthDate;

    @ManyToMany(mappedBy = "residents", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Flat> flats = new HashSet<>();

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
