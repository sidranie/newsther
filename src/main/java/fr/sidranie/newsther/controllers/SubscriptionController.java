package fr.sidranie.newsther.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import fr.sidranie.newsther.dtos.subscription.FullSubscriptionDto;
import fr.sidranie.newsther.dtos.subscription.NewSubscriptionDto;
import fr.sidranie.newsther.entities.Subscription;
import fr.sidranie.newsther.mappers.SubscriptionMapper;
import fr.sidranie.newsther.services.SubscriptionService;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

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

    @PostMapping
    public ResponseEntity<FullSubscriptionDto> subscribePersonToNewsletter(@RequestBody NewSubscriptionDto newSubscriptionDto) {
        Subscription subscription = subscriptionService.subscribePersonToNewsletter(newSubscriptionDto.getPersonId(),
                newSubscriptionDto.getNewsletterId());
        FullSubscriptionDto fullSubscriptionDto = SubscriptionMapper.subscriptionToFullSubscriptionDto(subscription);
        return ResponseEntity.created(URI.create("/subscriptions/" + subscription.getId()))
                .body(fullSubscriptionDto);
    }
}
