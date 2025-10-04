package com.diploma.house.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "houses")
@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
@EnableJpaAuditing
public class House {

    @Id
    @Column
    private UUID id;

    @Column
    @NotBlank
    private String address;

    @ManyToOne
    @JoinColumn(name = "hoa_id")
    private Hoa hoa;

    @Column
    private double livingArea;

    @Column
    private int numOfFloors;

    @Column
    private int numOfSections;

    @Column
    private int numOfEntrances;

    @Column
    private int numOfFlats;

    @Column
    private int numOfOffices;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
