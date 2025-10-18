package com.diploma.house.mapper;

import com.diploma.house.dto.ContactDto;
import com.diploma.house.dto.ContactTypeDto;
import com.diploma.house.entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public ContactDto mapToContactDto(Contact contact) {
        ContactDto dto = new ContactDto();
        dto.setId(contact.getId());
        dto.setPersonId(contact.getPerson().getId());

        ContactTypeDto typeDTO = new ContactTypeDto();
        typeDTO.setId(contact.getContactType().getId());
        typeDTO.setContactType(contact.getContactType().getContactType());
        dto.setContactType(typeDTO);

        dto.setContact(contact.getContact());

        return dto;

    }

}
