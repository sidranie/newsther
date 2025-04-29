package fr.sidranie.newsther.services.impl;

import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.util.Optional;

import fr.sidranie.newsther.entities.Newsletter;
import fr.sidranie.newsther.entities.Person;
import fr.sidranie.newsther.entities.Subscription;
import fr.sidranie.newsther.repositories.PersonRepository;
import fr.sidranie.newsther.repositories.SubscriptionRepository;
import fr.sidranie.newsther.services.NewsletterService;
import fr.sidranie.newsther.services.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repository;
    private final PersonRepository personRepository;
    private final NewsletterService newsletterService;

    public SubscriptionServiceImpl(SubscriptionRepository repository,
                                   PersonRepository personRepository,
                                   NewsletterService newsletterService) {
        this.repository = repository;
        this.personRepository = personRepository;
        this.newsletterService = newsletterService;
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Subscription> findByPersonIdAndNewsletterId(Long personId, Long newsletterId) {
        return repository.findByPersonIdAndNewsletterId(personId, newsletterId);
    }

    @Override
    public Subscription subscribePersonToNewsletter(Long personId, Long newsletterId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(IllegalArgumentException::new);
        Newsletter newsletter = newsletterService.findById(newsletterId)
                .orElseThrow(IllegalArgumentException::new);

        Subscription subscription = new Subscription();
        subscription.setSince(Instant.now());
        subscription.setPerson(person);
        subscription.setNewsletter(newsletter);

        repository.save(subscription);

        return subscription;
    }

    @Override
    public Subscription subscribePersonToNewsletter(Principal principal, String slug) throws IllegalAccessException {
        Person person = personRepository.findByUsernameOrEmail(principal.getName(), principal.getName())
                .orElseThrow(IllegalAccessException::new);
        Newsletter newsletter = newsletterService.findBySlug(slug)
                .orElseThrow(IllegalArgumentException::new);
        return subscribePersonToNewsletter(person.getId(), newsletter.getId());
    }

    @Override
    public void unsubscribePersonFromNewsletter(Long personId, Long newsletterId) {
        Subscription subscription = repository.findByPersonIdAndNewsletterId(personId, newsletterId)
                .orElseThrow(IllegalArgumentException::new);
        repository.delete(subscription);
    }

    @Override
    public void unsubscribePersonFromNewsletter(Principal principal, String slug) throws IllegalAccessException {
        Person person = personRepository.findByUsernameOrEmail(principal.getName(), principal.getName())
                .orElseThrow(IllegalAccessException::new);
        Newsletter newsletter = newsletterService.findBySlug(slug)
                .orElseThrow(IllegalArgumentException::new);
        unsubscribePersonFromNewsletter(person.getId(), newsletter.getId());
    }

    @Override
    public void unsubscribePersonFromNewsletter(Long id) {
        repository.deleteById(id);
    }
}
