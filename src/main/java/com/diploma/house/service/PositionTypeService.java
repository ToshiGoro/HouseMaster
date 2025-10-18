package com.diploma.house.service;

import com.diploma.house.entity.PositionType;
import com.diploma.house.repository.PositionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PositionTypeService {

    private final PositionTypeRepository positionTypeRepository;

    public List<PositionType> getAllPositionTypes() {
        return positionTypeRepository.findAll();
    }

    public PositionType getPositionTypeById(UUID id) {
        return positionTypeRepository.findById(id).orElse(null);
    }

    public PositionType createPositionType(PositionType positionType) {
        return positionTypeRepository.save(positionType);
    }

    public PositionType updatePositionType(UUID id, PositionType positionTypeDetails) {

        PositionType positionType = positionTypeRepository.findById(id).orElse(null);
        if (positionType != null) {
            positionType.setPositionType(positionTypeDetails.getPositionType());
            return positionTypeRepository.save(positionType);
        }

        return null;

    }

    public boolean deletePositionType(UUID id) {

        if (positionTypeRepository.existsById(id)) {
            positionTypeRepository.deleteById(id);
            return true;
        }

        return false;

    }

}
