package com.diploma.house.service;

import com.diploma.house.dto.PersonResponseDto;
import com.diploma.house.entity.Person;
import com.diploma.house.mapper.PersonMapper;
import com.diploma.house.repository.PersonRepository;
import com.diploma.house.request.PersonRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Validated
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

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

//        personRepository.saveAndFlush(person);

        return personMapper.mapToPersonResponseDto(person);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePerson(UUID id) {

        personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String
                .format(PERSON_NOT_FOUND, id)));
        personRepository.deleteById(id);

    }

}
