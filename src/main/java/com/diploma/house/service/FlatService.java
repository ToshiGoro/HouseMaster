package com.diploma.house.service;

import com.diploma.house.dto.FlatResponseDto;
import com.diploma.house.entity.Flat;
import com.diploma.house.entity.House;
import com.diploma.house.entity.Person;
import com.diploma.house.mapper.FlatMapper;
import com.diploma.house.repository.FlatRepository;
import com.diploma.house.repository.HouseRepository;
import com.diploma.house.repository.PersonRepository;
import com.diploma.house.request.FlatRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FlatService {

    private final FlatRepository flatRepository;
    private final HouseRepository houseRepository;
    private final FlatMapper flatMapper;
    private final PersonRepository personRepository;

    private static final String FLAT_NOT_FOUND = "Квартира с id %s не найдена";
    private static final String HOUSE_NOT_FOUND = "Дом с id %s не найден";

    public FlatResponseDto getFlat(UUID id) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(FLAT_NOT_FOUND, id)));
        return flatMapper.mapToFlatResponseDto(flat);
    }

    public List<FlatResponseDto> getFlatsByHouse(UUID houseId) {
        // Проверяем что дом существует
        if (!houseRepository.existsById(houseId)) {
            throw new EntityNotFoundException(String.format(HOUSE_NOT_FOUND, houseId));
        }

        List<Flat> flats = flatRepository.findByHouseId(houseId);
        return flats.stream()
                .map(flatMapper::mapToFlatResponseDto)
                .toList();
    }

    @Transactional
    public FlatResponseDto updateFlat(UUID id, FlatRequest request) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(FLAT_NOT_FOUND, id)));

        return flatMapper.mapToFlatResponseDto(flat);

    }

    @Transactional
    public void removeResidentFromFlat(UUID flatId, UUID personId) {
        int deletedCount = flatRepository.deleteResidentFromFlat(flatId, personId);
        if (deletedCount == 0) {
            throw new IllegalStateException("Человек не проживает в этой квартире");
        }
    }

    @Transactional
    public void addResidentToFlat(UUID flatId, UUID personId) {
        boolean exists = flatRepository.existsResidentInFlat(flatId, personId);
        if (exists) {
            throw new IllegalStateException("Человек уже проживает в этой квартире");
        }
        flatRepository.addResidentToFlat(flatId, personId);
    }

}
