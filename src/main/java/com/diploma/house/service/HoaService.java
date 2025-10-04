package com.diploma.house.service;

import com.diploma.house.dto.HoaResponseDto;
import com.diploma.house.entity.Hoa;
import com.diploma.house.mapper.HoaMapper;
import com.diploma.house.repository.HoaRepository;
import com.diploma.house.request.HoaRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Validated
public class HoaService {

    @Autowired
    HoaRepository hoaRepository;

    @Autowired
    HoaMapper hoaMapper;

    @Transactional(rollbackFor = Exception.class)
    public HoaResponseDto createHoa(@Valid HoaRequest request) {

        Hoa hoa = new Hoa(UUID.randomUUID(), request.getName(), request.getCreationDate(), request.getLiquidationDate(),
                null);
        hoaRepository.saveAndFlush(hoa);

        return hoaMapper.mapToHoaResponseDto(hoa);

    }

    public HoaResponseDto getHoa(UUID id) {

        Hoa hoa = hoaRepository.findById(id).orElseThrow();

        return hoaMapper.mapToHoaResponseDto(hoa);
    }

    @Transactional(rollbackFor = Exception.class)
    public HoaResponseDto updateHoa(UUID id, @Valid HoaRequest request) {

        Hoa hoa = hoaRepository.findById(id).orElseThrow();

        hoa.setName(request.getName());
        hoa.setCreationDate(request.getCreationDate());
        hoa.setLiquidationDate(request.getLiquidationDate());

        hoaRepository.saveAndFlush(hoa);

        return hoaMapper.mapToHoaResponseDto(hoa);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteHoa(UUID id) {

        hoaRepository.findById(id).orElseThrow();
        hoaRepository.deleteById(id);

    }

}
