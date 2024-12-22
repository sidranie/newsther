package fr.sidranie.newsther.mappers;

import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;
import fr.sidranie.newsther.dtos.person.ShortPersonDto;
import fr.sidranie.newsther.dtos.subscription.FullSubscriptionDto;
import fr.sidranie.newsther.dtos.subscription.ShortNewsletterSubscriptionDto;
import fr.sidranie.newsther.dtos.subscription.ShortPersonSubscriptionDto;
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

        ShortPersonDto person = PersonMapper.personToShortPersonDto(subscription.getPerson());
        fullSubscriptionDto.setPerson(person);

        ShortNewsletterDto newsletter = NewsletterMapper.newsletterToShortNewsletterDto(subscription.getNewsletter());
        fullSubscriptionDto.setNewsletter(newsletter);

        return fullSubscriptionDto;
    }

    public static ShortNewsletterSubscriptionDto subscriptionToShortNewsletterSubscriptionDto(Subscription subscription) {
        ShortNewsletterSubscriptionDto subscriptionDto = new ShortNewsletterSubscriptionDto();
        subscriptionDto.setId(subscription.getId());
        subscriptionDto.setSince(subscription.getSince());

        ShortNewsletterDto newsletter = NewsletterMapper.newsletterToShortNewsletterDto(subscription.getNewsletter());
        subscriptionDto.setNewsletter(newsletter);

        return subscriptionDto;
    }

    public static ShortPersonSubscriptionDto subscriptionToShortPersonSubscriptionDto(Subscription subscription) {
        ShortPersonSubscriptionDto subscriptionDto = new ShortPersonSubscriptionDto();
        subscriptionDto.setId(subscription.getId());
        subscriptionDto.setSince(subscription.getSince());

        ShortPersonDto person = PersonMapper.personToShortPersonDto(subscription.getPerson());
        subscriptionDto.setPerson(person);

        return subscriptionDto;
    }
}
