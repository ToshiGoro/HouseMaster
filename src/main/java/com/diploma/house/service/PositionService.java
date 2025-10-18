package com.diploma.house.service;

import com.diploma.house.entity.Position;
import com.diploma.house.entity.Person;
import com.diploma.house.entity.PositionType;
import com.diploma.house.entity.House;
import com.diploma.house.repository.PositionRepository;
import com.diploma.house.repository.PersonRepository;
import com.diploma.house.repository.PositionTypeRepository;
import com.diploma.house.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final PersonRepository personRepository;
    private final PositionTypeRepository positionTypeRepository;
    private final HouseRepository houseRepository;

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Position getPositionById(UUID id) {
        return positionRepository.findById(id).orElse(null);
    }

    public List<Position> getPositionsByHouse(UUID houseId) {
        return positionRepository.findByHouseId(houseId);
    }

    public List<Position> getPositionsByPerson(UUID personId) {
        return positionRepository.findByPersonId(personId);
    }

    public Position createPosition(UUID personId, UUID positionTypeId, UUID houseId) {

        Person person = personRepository.findById(personId).orElse(null);
        PositionType positionType = positionTypeRepository.findById(positionTypeId).orElse(null);
        House house = houseRepository.findById(houseId).orElse(null);

        if (person == null || positionType == null || house == null) {
            return null;
        }

        if (positionRepository.existsByPersonIdAndPositionTypeIdAndHouseId(personId, positionTypeId, houseId)) {
            return null;
        }

        Position position = new Position();
        position.setId(UUID.randomUUID());
        position.setPerson(person);
        position.setPositionType(positionType);
        position.setHouse(house);

        return positionRepository.save(position);

    }

    public boolean deletePosition(UUID id) {

        if (positionRepository.existsById(id)) {
            positionRepository.deleteById(id);
            return true;
        }

        return false;

    }

}
