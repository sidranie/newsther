package fr.sidranie.newsther.subscriptions;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    Optional<Subscription> findByPersonIdAndNewsletterId(Long personId, Long newsletterId);
    void deleteByNewsletterId(Long newsletterId);
    void deleteByPersonId(Long personId);
}
