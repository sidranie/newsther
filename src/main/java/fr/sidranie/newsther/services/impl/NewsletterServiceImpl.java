package fr.sidranie.newsther.services.impl;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import fr.sidranie.newsther.entities.Newsletter;
import fr.sidranie.newsther.repositories.NewsletterRepository;
import fr.sidranie.newsther.services.NewsletterService;

@Service
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository repository;

    public NewsletterServiceImpl(NewsletterRepository repository) {
        this.repository = repository;
    }

    @Override
    public Set<Newsletter> findAll() {
        Set<Newsletter> newsletters = new HashSet<>();
        repository.findAll().forEach(newsletters::add);
        return newsletters;
    }

    @Override
    public Optional<Newsletter> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void createNewsletter(Newsletter newsletter) {
        newsletter.setId(null);
        repository.save(newsletter);
    }

    @Override
    public void deleteNewsletter(Long id) {
        repository.deleteById(id);
    }
}
