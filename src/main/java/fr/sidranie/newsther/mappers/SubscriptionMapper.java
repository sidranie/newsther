package fr.sidranie.newsther.mappers;

import fr.sidranie.newsther.dtos.subscription.FullSubscriptionDto;
import fr.sidranie.newsther.dtos.subscription.ShortSubscriptionDto;
import fr.sidranie.newsther.entities.Subscription;

public class SubscriptionMapper {

    public static ShortSubscriptionDto subscriptionToShortSubscriptionDto(Subscription subscription) {
        ShortSubscriptionDto shortSubscriptionDto = new ShortSubscriptionDto();
        shortSubscriptionDto.setId(subscription.getId());
        shortSubscriptionDto.setSince(subscription.getSince());
        return shortSubscriptionDto;
    }

    public static FullSubscriptionDto subscriptionToFullSubscriptionDto(Subscription subscription) {
        FullSubscriptionDto fullSubscriptionDto = new FullSubscriptionDto();
        fullSubscriptionDto.setId(subscription.getId());
        fullSubscriptionDto.setSince(subscription.getSince());
        fullSubscriptionDto.setPerson(PersonMapper.personToShortPersonDto(subscription.getPerson()));
        return fullSubscriptionDto;
    }
}
