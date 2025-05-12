package fr.sidranie.newsther.subscriptions;

import java.security.Principal;
import java.time.Instant;

import org.springframework.stereotype.Service;
import fr.sidranie.newsther.newsletters.Newsletter;
import fr.sidranie.newsther.newsletters.Newsletters;
import fr.sidranie.newsther.people.People;
import fr.sidranie.newsther.people.Person;

@Service
public class SubscriptionService {

    private final Subscriptions subscriptions;
    private final People people;
    private final Newsletters newsletters;

    public SubscriptionService(Subscriptions subscriptions,
                               People people,
                               Newsletters newsletters) {
        this.subscriptions = subscriptions;
        this.people = people;
        this.newsletters = newsletters;
    }

    public Subscription subscribePersonToNewsletter(Long personId, Long newsletterId) {
        Person person = people.findById(personId)
                .orElseThrow(IllegalArgumentException::new);
        Newsletter newsletter = newsletters.findById(newsletterId)
                .orElseThrow(IllegalArgumentException::new);

        Subscription subscription = new Subscription();
        subscription.setSince(Instant.now());
        subscription.setPerson(person);
        subscription.setNewsletter(newsletter);

        subscriptions.save(subscription);

        return subscription;
    }

    public Subscription subscribePersonToNewsletter(Principal principal, String slug) throws IllegalAccessException {
        Person person = people.findByUsernameOrEmail(principal.getName(), principal.getName())
                .orElseThrow(IllegalAccessException::new);
        Newsletter newsletter = newsletters.findBySlug(slug)
                .orElseThrow(IllegalArgumentException::new);
        return subscribePersonToNewsletter(person.getId(), newsletter.getId());
    }

    public void unsubscribePersonFromNewsletter(Long personId, Long newsletterId) {
        Subscription subscription = subscriptions.findByPersonIdAndNewsletterId(personId, newsletterId)
                .orElseThrow(IllegalArgumentException::new);
        subscriptions.delete(subscription);
    }

    public void unsubscribePersonFromNewsletter(Principal principal, String slug) throws IllegalAccessException {
        Person person = people.findByUsernameOrEmail(principal.getName(), principal.getName())
                .orElseThrow(IllegalAccessException::new);
        Newsletter newsletter = newsletters.findBySlug(slug)
                .orElseThrow(IllegalArgumentException::new);
        unsubscribePersonFromNewsletter(person.getId(), newsletter.getId());
    }
}
