package fr.sidranie.newsther.services.impl;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import fr.sidranie.newsther.entities.Person;
import fr.sidranie.newsther.entities.Subscription;
import fr.sidranie.newsther.repositories.SubscriptionRepository;
import fr.sidranie.newsther.services.PersonService;
import fr.sidranie.newsther.services.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private SubscriptionRepository repository;
    private PersonService personService;

    public SubscriptionServiceImpl(SubscriptionRepository repository, PersonService personService) {
        this.repository = repository;
        this.personService = personService;
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Subscription subscribePersonToNewsletter(Long personId) {
        Person person = personService.findById(personId)
                .orElseThrow(IllegalArgumentException::new);

        Subscription subscription = new Subscription();
        subscription.setSince(Instant.now());
        subscription.setPerson(person);

        repository.save(subscription);

        return subscription;
    }

    @Override
    public void unsubscribePersonFromNewsletter(Long personId) {
        // Not implemented
    }
}
