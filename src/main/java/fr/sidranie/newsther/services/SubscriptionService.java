package fr.sidranie.newsther.services;

import java.security.Principal;
import java.util.Optional;

import fr.sidranie.newsther.entities.Subscription;

public interface SubscriptionService {
    Optional<Subscription> findById(Long id);

    Optional<Subscription> findByPersonIdAndNewsletterId(Long personId, Long newsletterId);

    Subscription subscribePersonToNewsletter(Long personId, Long newsletterId);

    Subscription subscribePersonToNewsletter(Principal principal, String slug) throws IllegalAccessException;

    void unsubscribePersonFromNewsletter(Long personId, Long newsletterId);

    void unsubscribePersonFromNewsletter(Principal principal, String slug) throws IllegalAccessException;

    void unsubscribePersonFromNewsletter(Long id);

}
