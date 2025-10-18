package com.diploma.house.controller;

import com.diploma.house.dto.HoaForHouseDisplayResponseDto;
import com.diploma.house.dto.HoaResponseDto;
import com.diploma.house.dto.HouseResponseDto;
import com.diploma.house.request.HoaRequest;
import com.diploma.house.service.HoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/hoas")
@Validated
public class HoaController {

    @Autowired
    HoaService hoaService;

    @PostMapping("/create")
    public ResponseEntity<HoaResponseDto> createHoa(@Valid @RequestBody HoaRequest request, UUID houseId) {

        HoaResponseDto customerResponseDto = hoaService.createHoa(request, houseId);

        return ResponseEntity.ok(customerResponseDto);

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HoaResponseDto> getHoa(@PathVariable UUID id) {

        HoaResponseDto customerResponseDto = hoaService.getHoa(id);

        return ResponseEntity.ok(customerResponseDto);

    }

    @GetMapping("/getAllHoaForHouseDisplay")
    public ResponseEntity<List<HoaForHouseDisplayResponseDto>> getAllHoaForHouseDisplay() {

        List<HoaForHouseDisplayResponseDto> dtos = hoaService.getHoaForHouseDisplay();

        return ResponseEntity.ok(dtos);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HoaResponseDto> updateHoa(@PathVariable UUID id,
                                                        @Valid @RequestBody HoaRequest request) {

        HoaResponseDto updatedHoa = hoaService.updateHoa(id, request);

        return ResponseEntity.ok(updatedHoa);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteHoa(@PathVariable UUID id) {

        hoaService.deleteHoa(id);

        return ResponseEntity.noContent().build();

    }

}
