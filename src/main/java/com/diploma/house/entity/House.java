package com.diploma.house.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "houses")
@Getter
@Setter
@ToString(exclude = "hoa")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class House {

    @Id
    @EqualsAndHashCode.Include
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "Адрес дома обязателен для заполнения")
    private String address;

    // Связь может быть nullable - дом может существовать без ТСЖ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_id")
    private Hoa hoa;

    @Column
    private Double livingArea;

    @Column
    private Integer numOfFloors;

    @Column
    private Integer numOfSections;

    @Column
    private Integer numOfEntrances;

    @Column
    private Integer numOfFlats;

    @Column
    private Integer numOfOffices;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Метод для присоединения к ТСЖ
    public void joinHoa(Hoa hoa) {

        if (this.hoa != null) {
            this.hoa.removeHouse(this); // Выходим из старого ТСЖ
        }

        this.hoa = hoa;

        if (hoa != null && !hoa.getHouses().contains(this)) {
            hoa.addHouse(this); // Добавляем в новое ТСЖ
        }

    }

    // Метод для выхода из ТСЖ
    public void leaveHoa() {

        if (this.hoa != null) {
            Hoa currentHoa = this.hoa;
            this.hoa = null;
            currentHoa.removeHouse(this);
        }

    }

}
