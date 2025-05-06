package fr.sidranie.newsther.services;

import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.Set;

import fr.sidranie.newsther.entities.Person;

public interface PersonService extends UserDetailsService, UserDetailsPasswordService {
    Set<Person> findAll();

    Optional<Person> findById(Long id);

    Optional<Person> findByUsernameOrEmail(String identifier);

    void registerPerson(Person person);

    Person updatePerson(Person person, Person personUpdates);

    void deleteById(Long id);
}
