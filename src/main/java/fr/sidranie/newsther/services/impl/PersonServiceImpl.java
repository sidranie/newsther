package fr.sidranie.newsther.services.impl;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import fr.sidranie.newsther.entities.Person;
import fr.sidranie.newsther.repositories.PersonRepository;
import fr.sidranie.newsther.services.PersonService;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository repository;

    public PersonServiceImpl(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Set<Person> findAll() {
        Set<Person> people = new HashSet<>();
        repository.findAll().forEach(people::add);
        return people;
    }

    @Override
    public Optional<Person> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void registerPerson(Person person) {
        person.setId(null);
        repository.save(person);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
