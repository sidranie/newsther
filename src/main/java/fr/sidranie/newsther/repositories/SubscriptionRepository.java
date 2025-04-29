package fr.sidranie.newsther.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import fr.sidranie.newsther.entities.Subscription;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    Optional<Subscription> findByPersonIdAndNewsletterId(Long personId, Long newsletterId);
    void deleteByNewsletterId(Long newsletterId);
    void deleteByPersonId(Long personId);
}
