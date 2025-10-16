package com.diploma.house.service;

import com.diploma.house.dto.HouseForStartPageResponseDto;
import com.diploma.house.dto.HouseResponseDto;
import com.diploma.house.entity.Flat;
import com.diploma.house.entity.Hoa;
import com.diploma.house.entity.House;
import com.diploma.house.mapper.HouseMapper;
import com.diploma.house.repository.FlatRepository;
import com.diploma.house.repository.HoaRepository;
import com.diploma.house.repository.HouseRepository;
import com.diploma.house.request.HouseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class HouseService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;
    private final HoaRepository hoaRepository;
    private final FlatRepository flatRepository;

    @Transactional(rollbackFor = Exception.class)
    public HouseResponseDto createHouse(@Valid HouseRequest request) {

        House house = new House(UUID.randomUUID(), request.getAddress(),
                request.getHoaId() == null ? null : hoaRepository.findById(request.getHoaId()).orElse(null),
                new ArrayList<>(), request.getLivingArea(), request.getNumOfFloors(), request.getNumOfSections(),
                request.getNumOfEntrances(), request.getNumOfFlats(), request.getNumOfOffices(), null);

        setHoaIfProvided(request, house);

        House savedHouse = houseRepository.save(house);

        // Сразу же создаём пул квартир, так как дом не может существовать без них
        List<Flat> flats = new ArrayList<>();
        for (int i = 1; i <= savedHouse.getNumOfFlats(); i++) {
            Flat flat = new Flat();
            flat.setId(UUID.randomUUID());
            flat.setFlatNumber(i);
            flat.setHouse(savedHouse);
            flats.add(flat);
        }
        flatRepository.saveAll(flats);

        return houseMapper.mapToHouseResponseDto(savedHouse);

    }

    public HouseResponseDto getHouse(UUID id) {

        House house = houseRepository.findById(id).orElseThrow();

        return houseMapper.mapToHouseResponseDto(house);

    }

    @Transactional(rollbackFor = Exception.class)
    public HouseResponseDto updateHouse(UUID id, @Valid HouseRequest request) {

        House house = houseRepository.findById(id).orElseThrow();

        house.setAddress(request.getAddress());
        house.setLivingArea(request.getLivingArea());
        house.setNumOfFloors(request.getNumOfFloors());
        house.setNumOfSections(request.getNumOfSections());
        house.setNumOfEntrances(request.getNumOfEntrances());
        house.setNumOfFlats(request.getNumOfFlats());
        house.setNumOfOffices(request.getNumOfOffices());

        setHoaIfProvided(request, house);

        houseRepository.saveAndFlush(house);

        return houseMapper.mapToHouseResponseDto(house);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteHouse(UUID id) {

        houseRepository.findById(id).orElseThrow();
        houseRepository.deleteById(id);

    }

    public List<HouseResponseDto> getAllHouses() {

        List<House> houses = houseRepository.findAll();

        return houses.stream()
                .map(e -> houseMapper.mapToHouseResponseDto(e))
                .toList();

    }

    /**
     * Если при создании или изменении дома (House) передаётся идентификатор ТСЖ (hoaId), метод вносит объект hoa
     * в соответствующее поле объекта House, устанавливая таким образом реляционную связь.
     * @param request запрос дома
     * @param house сущность дома
     */
    private void setHoaIfProvided(HouseRequest request, House house) {

        if (request.getHoaId() != null) {
            Hoa hoa = hoaRepository.findById(request.getHoaId())
                    .orElseThrow(() -> new EntityNotFoundException("ТСЖ не найдено"));
            house.setHoa(hoa);
        } else {
            house.setHoa(null);
        }

    }

    public List<HouseForStartPageResponseDto> getAllHousesForStartPage() {

        List<House> houses = houseRepository.findAll();

        return houses.stream()
                .map(e -> houseMapper.mapToHouseForStartPageResponseDto(e))
                .toList();

    }

}
