package fr.sidranie.newsther.people;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface People extends JpaRepository<Person, Long> {
    Optional<Person> findByUsernameOrEmail(String username, String email);
}
