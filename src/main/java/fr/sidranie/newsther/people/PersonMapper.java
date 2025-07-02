package fr.sidranie.newsther.people;

import java.util.Collections;

import fr.sidranie.newsther.people.dtos.CreatePersonDto;

public class PersonMapper {

    public static Person createPersonDtoToPerson(CreatePersonDto createPersonDto) {
        return new Person(
                createPersonDto.getUsername(),
                createPersonDto.getEmail(),
                createPersonDto.getPassword(),
                createPersonDto.getGivenName(),
                createPersonDto.getFamilyName()
        );
    }
}
