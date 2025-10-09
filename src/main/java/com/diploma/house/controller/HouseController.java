package com.diploma.house.controller;

import com.diploma.house.dto.HouseForStartPageResponseDto;
import com.diploma.house.dto.HouseResponseDto;
import com.diploma.house.request.HouseRequest;
import com.diploma.house.service.HouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/houses")
@Validated
public class HouseController {

    @Autowired
    HouseService houseService;

    @PostMapping("/create")
    public ResponseEntity<HouseResponseDto> createHouse(@Valid @RequestBody HouseRequest request) {

        HouseResponseDto houseResponseDto = houseService.createHouse(request);

        return ResponseEntity.ok(houseResponseDto);

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HouseResponseDto> getHouse(@PathVariable UUID id) {

        HouseResponseDto houseResponseDto = houseService.getHouse(id);

        return ResponseEntity.ok(houseResponseDto);

    }

    @GetMapping("/getAll")
    public ResponseEntity<List<HouseResponseDto>> getAllHouses() {

        List<HouseResponseDto> houseResponseDtos = houseService.getAllHouses();

        return ResponseEntity.ok(houseResponseDtos);

    }

    @GetMapping("/getAllForStartPage")
    public ResponseEntity<List<HouseForStartPageResponseDto>> getAllHousesForStartPage() {

        List<HouseForStartPageResponseDto> HouseForStartPageResponseDtos = houseService.getAllHousesForStartPage();

        return ResponseEntity.ok(HouseForStartPageResponseDtos);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HouseResponseDto> updateHouse(@PathVariable UUID id,
                                                        @Valid @RequestBody HouseRequest request) {

        HouseResponseDto updatedHouse = houseService.updateHouse(id, request);

        return ResponseEntity.ok(updatedHouse);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteHouse(@PathVariable UUID id) {

        houseService.deleteHouse(id);

        return ResponseEntity.noContent().build();

    }

}
