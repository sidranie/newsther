package fr.sidranie.newsther.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.sidranie.newsther.entities.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    // Empty body
}
