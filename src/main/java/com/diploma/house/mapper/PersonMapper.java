package com.diploma.house.mapper;

import com.diploma.house.dto.HouseResponseDto;
import com.diploma.house.dto.PersonResponseDto;
import com.diploma.house.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonResponseDto mapToPersonResponseDto(Person person) {
        return new PersonResponseDto(
                person.getId(),
                person.getFirstName(),
                person.getSecondName(),
                person.getLastName(),
                person.getGender(),
                person.getBirthDate());
    }

}
