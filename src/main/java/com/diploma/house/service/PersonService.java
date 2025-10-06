package com.diploma.house.service;

import com.diploma.house.dto.PersonResponseDto;
import com.diploma.house.entity.Person;
import com.diploma.house.mapper.PersonMapper;
import com.diploma.house.repository.PersonRepository;
import com.diploma.house.request.PersonRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Validated
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonMapper personMapper;

    @Transactional(rollbackFor = Exception.class)
    public PersonResponseDto createPerson(@Valid PersonRequest request) {

        Person person = new Person(UUID.randomUUID(), request.getFirstName(), request.getSecondName(),
                request.getLastName(), request.getGender(), request.getBirthDate(), null);
        personRepository.saveAndFlush(person);

        return personMapper.mapToPersonResponseDto(person);

    }

    public PersonResponseDto getPerson(UUID id) {

        Person person = personRepository.findById(id).orElseThrow();

        return personMapper.mapToPersonResponseDto(person);
    }

    @Transactional(rollbackFor = Exception.class)
    public PersonResponseDto updatePerson(UUID id, @Valid PersonRequest request) {

        Person person = personRepository.findById(id).orElseThrow();

        person.setFirstName(request.getFirstName());
        person.setSecondName(request.getSecondName());
        person.setLastName(request.getLastName());
        person.setGender(request.getGender());
        person.setBirthDate(request.getBirthDate());

        personRepository.saveAndFlush(person);

        return personMapper.mapToPersonResponseDto(person);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePerson(UUID id) {

        personRepository.findById(id).orElseThrow();
        personRepository.deleteById(id);

    }

}
