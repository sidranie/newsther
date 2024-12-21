package fr.sidranie.newsther.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.sidranie.newsther.entities.Subscription;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    // Empty body
}
