package fr.sidranie.newsther.services;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import fr.sidranie.newsther.entities.Newsletter;

public interface NewsletterService {
    Set<Newsletter> findAll();

    Optional<Newsletter> findById(Long id);

    Optional<Newsletter> findBySlug(String slug);

    void createNewsletter(Newsletter newsletter, Principal principal);

    void deleteNewsletter(Long id);
}
