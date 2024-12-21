package fr.sidranie.newsther.services;

import java.util.Optional;
import java.util.Set;

import fr.sidranie.newsther.entities.Person;

public interface PersonService {
    Set<Person> findAll();

    Optional<Person> findById(Long id);

    void registerPerson(Person person);

    void deleteById(Long id);
}
