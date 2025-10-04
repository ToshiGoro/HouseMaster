package com.diploma.house.service;

import com.diploma.house.dto.HouseResponseDto;
import com.diploma.house.entity.House;
import com.diploma.house.mapper.HouseMapper;
import com.diploma.house.repository.HouseRepository;
import com.diploma.house.request.HouseRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Validated
public class HouseService {

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    HouseMapper houseMapper;

    @Transactional(rollbackFor = Exception.class)
    public HouseResponseDto createHouse(@Valid HouseRequest request) {

        House house = new House(UUID.randomUUID(), request.getAddress(), request.getHoa(),
                request.getLivingArea(), request.getNumOfFloors(), request.getNumOfSections(),
                request.getNumOfEntrances(), request.getNumOfFlats(), request.getNumOfOffices(), null);
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
        house.setHoaId(request.getHoaId());
        house.setLivingArea(request.getLivingArea());
        house.setNumOfFloors(request.getNumOfFloors());
        house.setNumOfSections(request.getNumOfSections());
        house.setNumOfEntrances(request.getNumOfEntrances());
        house.setNumOfFlats(request.getNumOfFlats());
        house.setNumOfOffices(request.getNumOfOffices());

        houseRepository.saveAndFlush(house);

        return houseMapper.mapToHouseResponseDto(house);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteHouse(UUID id) {

        houseRepository.findById(id).orElseThrow();
        houseRepository.deleteById(id);

    }

}
