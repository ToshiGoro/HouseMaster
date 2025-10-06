package com.diploma.house.entity;

import com.diploma.house.audits.HoaEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "hoas")
@Getter
@Setter
@ToString(exclude = "houses")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners({AuditingEntityListener.class, HoaEntityListener.class})
public class Hoa {

    @Id
    @EqualsAndHashCode.Include
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "Необходимо указать наименование ТСЖ!")
    private String name;

    @Column
    private LocalDateTime creationDate;

    @Column
    private LocalDateTime liquidationDate;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "hoa",
            fetch = FetchType.LAZY
    )

    private List<House> houses = new ArrayList<>();

    public void addHouse(House house) {

        if (!houses.contains(house)) {
            houses.add(house);
            house.setHoa(this);
        }

    }

    public void removeHouse(House house) {
        houses.remove(house);
        house.setHoa(null);
    }

    public void clearHouses() {
        for (House house : new ArrayList<>(houses)) {
            removeHouse(house);
        }
    }

}


