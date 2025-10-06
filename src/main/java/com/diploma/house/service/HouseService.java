package com.diploma.house.service;

import com.diploma.house.dto.HouseResponseDto;
import com.diploma.house.entity.Hoa;
import com.diploma.house.entity.House;
import com.diploma.house.mapper.HouseMapper;
import com.diploma.house.repository.HoaRepository;
import com.diploma.house.repository.HouseRepository;
import com.diploma.house.request.HouseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Validated
public class HouseService {

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    HouseMapper houseMapper;

    @Autowired
    HoaRepository hoaRepository;

    @Transactional(rollbackFor = Exception.class)
    public HouseResponseDto createHouse(@Valid HouseRequest request) {

        House house = new House(UUID.randomUUID(), request.getAddress(), null,
                request.getLivingArea(), request.getNumOfFloors(), request.getNumOfSections(),
                request.getNumOfEntrances(), request.getNumOfFlats(), request.getNumOfOffices(), null);

        setHoaIfProvided(request, house);

        houseRepository.saveAndFlush(house);

        return houseMapper.mapToHouseResponseDto(house);

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
        }

    }

}
