package fr.sidranie.newsther.repositories;

import fr.sidranie.newsther.entities.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    // Empty interface
}