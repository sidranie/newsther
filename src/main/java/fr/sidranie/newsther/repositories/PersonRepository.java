package fr.sidranie.newsther.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import fr.sidranie.newsther.entities.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    Optional<Person> findByUsernameOrEmail(String username, String email);
}
