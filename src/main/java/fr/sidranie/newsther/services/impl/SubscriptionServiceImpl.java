package fr.sidranie.newsther.services.impl;

import java.security.Principal;
import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import fr.sidranie.newsther.entities.Newsletter;
import fr.sidranie.newsther.entities.Person;
import fr.sidranie.newsther.entities.Subscription;
import fr.sidranie.newsther.repositories.NewsletterRepository;
import fr.sidranie.newsther.repositories.PersonRepository;
import fr.sidranie.newsther.repositories.SubscriptionRepository;
import fr.sidranie.newsther.services.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repository;
    private final PersonRepository personRepository;
    private final NewsletterRepository newsletterRepository;

    public SubscriptionServiceImpl(SubscriptionRepository repository,
                                   PersonRepository personRepository,
                                   NewsletterRepository newsletterRepository) {
        this.repository = repository;
        this.personRepository = personRepository;
        this.newsletterRepository = newsletterRepository;
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
        Newsletter newsletter = newsletterRepository.findById(newsletterId)
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
        Newsletter newsletter = newsletterRepository.findBySlug(slug)
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
        Newsletter newsletter = newsletterRepository.findBySlug(slug)
                .orElseThrow(IllegalArgumentException::new);
        unsubscribePersonFromNewsletter(person.getId(), newsletter.getId());
    }

    @Override
    public void unsubscribePersonFromNewsletter(Long id) {
        repository.deleteById(id);
    }
}
