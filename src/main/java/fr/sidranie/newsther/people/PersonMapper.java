package fr.sidranie.newsther.people;

import java.util.Collections;

import fr.sidranie.newsther.people.dtos.CreatePersonDto;

public class PersonMapper {

    public static Person createPersonDtoToPerson(CreatePersonDto createPersonDto) {
        Person person = new Person();
        person.setUsername(createPersonDto.getUsername());
        person.setEmail(createPersonDto.getEmail());
        person.setPassword(createPersonDto.getPassword());
        person.setGivenName(createPersonDto.getGivenName());
        person.setFamilyName(createPersonDto.getFamilyName());
        person.setSubscriptions(Collections.emptySet());
        return person;
    }
}
