package fr.sidranie.newsther.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.sidranie.newsther.entities.Newsletter;

@Repository
public interface NewsletterRepository extends CrudRepository<Newsletter, Long> {
    Optional<Newsletter> findBySlug(String slug);
}
