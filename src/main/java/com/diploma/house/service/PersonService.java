package com.diploma.house.service;

import com.diploma.house.dto.PersonResponseDto;
import com.diploma.house.dto.ResidentInfoDto;
import com.diploma.house.entity.Flat;
import com.diploma.house.entity.Person;
import com.diploma.house.mapper.PersonMapper;
import com.diploma.house.repository.FlatRepository;
import com.diploma.house.repository.PersonRepository;
import com.diploma.house.request.PersonRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Validated
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final FlatRepository flatRepository;

    private static final String PERSON_NOT_FOUND = "Человек с id %s отсутствует в базе данных!";

    @Transactional(rollbackFor = Exception.class)
    public PersonResponseDto createPerson(@Valid PersonRequest request) {

        Person person = new Person(
                UUID.randomUUID(),
                request.getFirstName(),
                request.getSecondName(),
                request.getLastName(),
                request.getGender(),
                request.getBirthDate(),
                new HashSet<>(),
                new HashSet<>(),
                null
        );

        Person savedPerson = personRepository.save(person);

        return personMapper.mapToPersonResponseDto(savedPerson);

    }

    public PersonResponseDto getPerson(UUID id) {

        Person person = personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String
                .format(PERSON_NOT_FOUND, id)));

        return personMapper.mapToPersonResponseDto(person);

    }

    @Transactional(rollbackFor = Exception.class)
    public PersonResponseDto updatePerson(UUID id, @Valid PersonRequest request) {

        Person person = personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String
                .format(PERSON_NOT_FOUND, id)));

        person.setFirstName(request.getFirstName());
        person.setSecondName(request.getSecondName());
        person.setLastName(request.getLastName());
        person.setGender(request.getGender());
        person.setBirthDate(request.getBirthDate());

        return personMapper.mapToPersonResponseDto(person);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePerson(UUID id) {

        personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String
                .format(PERSON_NOT_FOUND, id)));
        personRepository.deleteById(id);

    }

    public List<PersonResponseDto> getAllPersons() {

        List<Person> persons = personRepository.findAll();

        return persons.stream()
                .map(personMapper::mapToPersonResponseDto)
                .toList();

    }

    public List<ResidentInfoDto> getResidentsByHouseId(UUID houseId) {

        List<Flat> houseFlats = flatRepository.findByHouseId(houseId);

        Map<UUID, Person> uniqueResidents = new HashMap<>();

        for (Flat flat : houseFlats) {
            for (Person resident : flat.getResidents()) {
                uniqueResidents.putIfAbsent(resident.getId(), resident);
            }
        }

        return uniqueResidents.values().stream()
                .map(this::mapToResidentInfoDto)
                .sorted(Comparator.comparing(ResidentInfoDto::getFullName))
                .collect(Collectors.toList());
    }

    private ResidentInfoDto mapToResidentInfoDto(Person person) {

        ResidentInfoDto dto = new ResidentInfoDto();

        dto.setId(person.getId());
        dto.setFullName(getFullName(person));
        dto.setGender(person.getGender());
        dto.setAge(calculateAge(person.getBirthDate()));

        List<ResidentInfoDto.FlatInfoDTO> ownedFlats = person.getFlats().stream()
                .map(this::mapToFlatInfoDTO)
                .toList();
        dto.setOwnedFlats(ownedFlats);

        return dto;

    }

    private ResidentInfoDto.FlatInfoDTO mapToFlatInfoDTO(Flat flat) {

        ResidentInfoDto.FlatInfoDTO flatInfo = new ResidentInfoDto.FlatInfoDTO();

        flatInfo.setId(flat.getId());
        flatInfo.setFlatNumber(flat.getFlatNumber());
        flatInfo.setHouseAddress(flat.getHouse().getAddress());
        flatInfo.setHouseId(flat.getHouse().getId());

        return flatInfo;

    }

    private String getFullName(Person person) {

        return person.getLastName() + " " + person.getFirstName() +
                (person.getSecondName() != null ? " " + person.getSecondName() : "");

    }

    private Integer calculateAge(LocalDate birthDate) {

        if (birthDate == null) return null;

        return Period.between(birthDate, LocalDate.now()).getYears();

    }

    public boolean canDeletePerson(UUID personId) {

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Человек не найден"));

        return person.getFlats().isEmpty();

    }

    public List<ResidentInfoDto.FlatInfoDTO> getPersonResidences(UUID personId) {

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Человек не найден"));

        return person.getFlats().stream()
                .map(this::mapToFlatInfoDTO)
                .collect(Collectors.toList());

    }

}
