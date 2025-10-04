package com.diploma.house.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "persons")
@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
@EnableJpaAuditing
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

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
