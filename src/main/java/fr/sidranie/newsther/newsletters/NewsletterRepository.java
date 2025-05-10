package fr.sidranie.newsther.newsletters;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    Optional<Newsletter> findBySlug(String slug);
    Set<Newsletter> findByCreatorId(Long creatorId);
}
