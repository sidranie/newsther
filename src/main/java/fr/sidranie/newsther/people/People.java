package fr.sidranie.newsther.people;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface People extends CrudRepository<Person, Long> {
    Optional<Person> findByUsernameOrEmail(String username, String email);
}
