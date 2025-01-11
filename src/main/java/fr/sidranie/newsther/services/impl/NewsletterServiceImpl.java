package fr.sidranie.newsther.services.impl;

import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import fr.sidranie.newsther.entities.Newsletter;
import fr.sidranie.newsther.repositories.NewsletterRepository;
import fr.sidranie.newsther.services.NewsletterService;
import fr.sidranie.newsther.services.PersonService;

@Service
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository repository;
    private final PersonService personService;

    public NewsletterServiceImpl(NewsletterRepository repository, PersonService personService) {
        this.repository = repository;
        this.personService = personService;
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
    public Optional<Newsletter> findBySlug(String slug) {
        return repository.findBySlug(slug);
    }

    @Override
    public void createNewsletter(Newsletter newsletter, Principal principal) {
        newsletter.setId(null);
        newsletter.setCreator(personService.findByUsernameOrEmail(principal.getName())
                .orElseThrow(IllegalAccessError::new));
        newsletter.setSlug(nameToSlug(newsletter.getName()));
        repository.save(newsletter);
    }

    @Override
    public void deleteNewsletter(Long id) {
        repository.deleteById(id);
    }

    private String nameToSlug(String name) {
        final Pattern NONLATIN = Pattern.compile("[^\\w-]");
        final Pattern WHITESPACE = Pattern.compile("[\\s]");
        String nowhitespace = WHITESPACE.matcher(name).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.FRANCE);
    }
}
