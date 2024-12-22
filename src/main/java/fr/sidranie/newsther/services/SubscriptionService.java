package fr.sidranie.newsther.services;

import java.util.Optional;

import fr.sidranie.newsther.entities.Subscription;

public interface SubscriptionService {
    Optional<Subscription> findById(Long id);

    Optional<Subscription> findByPersonIdAndNewsletterId(Long personId, Long newsletterId);

    Subscription subscribePersonToNewsletter(Long personId, Long newsletterId);

    void unsubscribePersonFromNewsletter(Long personId, Long newsletterId);

    void unsubscribePersonFromNewsletter(Long id);

}
