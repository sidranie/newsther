package fr.sidranie.newsther.subscriptions;

import java.security.Principal;
import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import fr.sidranie.newsther.newsletters.Newsletter;
import fr.sidranie.newsther.newsletters.NewsletterRepository;
import fr.sidranie.newsther.people.Person;
import fr.sidranie.newsther.people.PersonRepository;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final PersonRepository personRepository;
    private final NewsletterRepository newsletterRepository;

    public SubscriptionService(SubscriptionRepository repository,
                               PersonRepository personRepository,
                               NewsletterRepository newsletterRepository) {
        this.repository = repository;
        this.personRepository = personRepository;
        this.newsletterRepository = newsletterRepository;
    }

    public Optional<Subscription> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Subscription> findByPersonIdAndNewsletterId(Long personId, Long newsletterId) {
        return repository.findByPersonIdAndNewsletterId(personId, newsletterId);
    }

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

    public Subscription subscribePersonToNewsletter(Principal principal, String slug) throws IllegalAccessException {
        Person person = personRepository.findByUsernameOrEmail(principal.getName(), principal.getName())
                .orElseThrow(IllegalAccessException::new);
        Newsletter newsletter = newsletterRepository.findBySlug(slug)
                .orElseThrow(IllegalArgumentException::new);
        return subscribePersonToNewsletter(person.getId(), newsletter.getId());
    }

    public void unsubscribePersonFromNewsletter(Long personId, Long newsletterId) {
        Subscription subscription = repository.findByPersonIdAndNewsletterId(personId, newsletterId)
                .orElseThrow(IllegalArgumentException::new);
        repository.delete(subscription);
    }

    public void unsubscribePersonFromNewsletter(Principal principal, String slug) throws IllegalAccessException {
        Person person = personRepository.findByUsernameOrEmail(principal.getName(), principal.getName())
                .orElseThrow(IllegalAccessException::new);
        Newsletter newsletter = newsletterRepository.findBySlug(slug)
                .orElseThrow(IllegalArgumentException::new);
        unsubscribePersonFromNewsletter(person.getId(), newsletter.getId());
    }

    public void unsubscribePersonFromNewsletter(Long id) {
        repository.deleteById(id);
    }
}
