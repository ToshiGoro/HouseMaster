package com.diploma.house.service;

import com.diploma.house.dto.HoaForHouseDisplayResponseDto;
import com.diploma.house.dto.HoaResponseDto;
import com.diploma.house.entity.Hoa;
import com.diploma.house.entity.House;
import com.diploma.house.mapper.HoaMapper;
import com.diploma.house.repository.HoaRepository;
import com.diploma.house.repository.HouseRepository;
import com.diploma.house.request.HoaRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Validated
public class HoaService {

    @Autowired
    HoaRepository hoaRepository;

    @Autowired
    HoaMapper hoaMapper;

    @Autowired
    HouseRepository houseRepository;

    @Transactional(rollbackFor = Exception.class)
    public HoaResponseDto createHoa(@Valid HoaRequest request, @Valid UUID houseId) {

        // 1. Получаем Entity дома из репозитория
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new EntityNotFoundException("Дом с ID " + houseId + " не найден"));

        // 2. Проверяем, не привязан ли дом уже к другому ТСЖ
        if (house.getHoa() != null) {
            throw new IllegalStateException(
                    "Дом по адресу " + house.getAddress() + " уже привязан к ТСЖ: " + house.getHoa().getName()
            );
        }

        // 3. Создаем ТСЖ
        Hoa hoa = new Hoa();
        hoa.setId(UUID.randomUUID());
        hoa.setName(request.getName());
        hoa.setCreationDate(request.getCreationDate());
        hoa.setLiquidationDate(request.getLiquidationDate());

        // 4. Сохраняем Hoa ПЕРВЫМ
        hoa = hoaRepository.save(hoa); // используем save(), а не saveAndFlush()

        // 5. Устанавливаем связь
        hoa.addHouse(house);

        // 6. Сохраняем House (опционально, т.к. @Transactional обновит изменения)
        houseRepository.save(house);

        return hoaMapper.mapToHoaResponseDto(hoa);

    }

    public HoaResponseDto getHoa(UUID id) {

        Hoa hoa = hoaRepository.findById(id).orElseThrow();

        return hoaMapper.mapToHoaResponseDto(hoa);
    }

    /**
     * Метод собирает коллекцию всех ТСЖ с набором базовых полей - id и название
     * @return Возвращает List объектов ТСЖ с полями id и наименование
     */
    public List<HoaForHouseDisplayResponseDto> getHoaForHouseDisplay() {

        List<Hoa> hoas = hoaRepository.findAll();

        return hoas.stream()
                .map(e -> hoaMapper.mapToHoaForHouseDisplayResponseDto(e))
                .toList();

    }

    @Transactional(rollbackFor = Exception.class)
    public HoaResponseDto updateHoa(UUID id, @Valid HoaRequest request) {

        // 1. Проверяем существование ТСЖ с обработкой исключения
        Hoa hoa = hoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ТСЖ с ID " + id + " не найдено"));

        // 2. Проверяем бизнес-логику (опционально)
        if (request.getLiquidationDate() != null &&
                request.getLiquidationDate().isBefore(request.getCreationDate())) {
            throw new IllegalArgumentException("Дата ликвидации не может быть раньше даты создания");
        }

        // 3. Обновляем только разрешенные поля
        hoa.setName(request.getName());
        hoa.setCreationDate(request.getCreationDate());
        hoa.setLiquidationDate(request.getLiquidationDate());

        // 4. save() не обязателен - изменения сохранятся благодаря @Transactional
        // hoaRepository.save(hoa);

        return hoaMapper.mapToHoaResponseDto(hoa);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteHoa(UUID id) {

        Hoa hoa = hoaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ТСЖ с ID " + id + " не найдено")
        );

        hoaRepository.delete(hoa);

    }

}
