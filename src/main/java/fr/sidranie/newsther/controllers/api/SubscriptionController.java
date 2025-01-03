package fr.sidranie.newsther.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import fr.sidranie.newsther.dtos.subscription.FullSubscriptionDto;
import fr.sidranie.newsther.dtos.subscription.IdsIdentifiedSubscriptionDto;
import fr.sidranie.newsther.entities.Subscription;
import fr.sidranie.newsther.mappers.SubscriptionMapper;
import fr.sidranie.newsther.services.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullSubscriptionDto> findById(@PathVariable("id") Long id) {
        return subscriptionService.findById(id)
                .map(SubscriptionMapper::subscriptionToFullSubscriptionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<FullSubscriptionDto> findByPersonIdAndNewsletterId(@RequestBody IdsIdentifiedSubscriptionDto idSubscription) {
        return subscriptionService.findByPersonIdAndNewsletterId(idSubscription.getPersonId(), idSubscription.getNewsletterId())
                .map(SubscriptionMapper::subscriptionToFullSubscriptionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FullSubscriptionDto> subscribePersonToNewsletter(@RequestBody IdsIdentifiedSubscriptionDto idSubscriptionDto) {
        Subscription subscription = subscriptionService.subscribePersonToNewsletter(idSubscriptionDto.getPersonId(),
                idSubscriptionDto.getNewsletterId());
        FullSubscriptionDto fullSubscriptionDto = SubscriptionMapper.subscriptionToFullSubscriptionDto(subscription);
        return ResponseEntity.created(URI.create("/subscriptions/" + subscription.getId()))
                .body(fullSubscriptionDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> unsubscribePersonToNewsletter(@RequestBody IdsIdentifiedSubscriptionDto idSubscriptionDto) {
        subscriptionService.unsubscribePersonFromNewsletter(idSubscriptionDto.getPersonId(), idSubscriptionDto.getNewsletterId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unsubscribePersonToNewsletter(@PathVariable("id") Long id) {
        subscriptionService.unsubscribePersonFromNewsletter(id);
        return ResponseEntity.noContent().build();
    }
}
