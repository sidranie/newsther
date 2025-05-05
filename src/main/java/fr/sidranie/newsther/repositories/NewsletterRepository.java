package fr.sidranie.newsther.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fr.sidranie.newsther.entities.Newsletter;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    Optional<Newsletter> findBySlug(String slug);
    Set<Newsletter> findByCreatorId(Long creatorId);
}
