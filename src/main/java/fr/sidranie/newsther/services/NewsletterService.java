package fr.sidranie.newsther.services;

import java.util.Optional;
import java.util.Set;

import fr.sidranie.newsther.entities.Newsletter;

public interface NewsletterService {
    Set<Newsletter> findAll();

    Optional<Newsletter> findById(Long id);

    void createNewsletter(Newsletter newsletter);

    void deleteNewsletter(Long id);
}
