package fr.sidranie.newsther.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import fr.sidranie.newsther.dtos.newsletter.CreateNewsletterDto;
import fr.sidranie.newsther.dtos.newsletter.FullNewsletterDto;
import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;
import fr.sidranie.newsther.dtos.subscription.ShortPersonSubscriptionDto;
import fr.sidranie.newsther.entities.Newsletter;

public class NewsletterMapper {

    public static ShortNewsletterDto newsletterToShortNewsletterDto(Newsletter newsletter) {
        ShortNewsletterDto shortNewsletterDto = new ShortNewsletterDto();
        shortNewsletterDto.setId(newsletter.getId());
        shortNewsletterDto.setName(newsletter.getName());
        shortNewsletterDto.setSlug(newsletter.getSlug());
        return shortNewsletterDto;
    }

    public static FullNewsletterDto newsletterToFullNewsletterDto(Newsletter newsletter) {
        FullNewsletterDto fullNewsletterDto = new FullNewsletterDto();
        fullNewsletterDto.setId(newsletter.getId());
        fullNewsletterDto.setName(newsletter.getName());
        fullNewsletterDto.setSlug(newsletter.getSlug());

        Set<ShortPersonSubscriptionDto> subscriptions = newsletter.getSubscriptions()
                .stream()
                .map(SubscriptionMapper::subscriptionToShortPersonSubscriptionDto)
                .collect(Collectors.toSet());
        fullNewsletterDto.setSubscriptions(subscriptions);

        return fullNewsletterDto;
    }

    public static Newsletter createNewsletterDtoToNewsletter(CreateNewsletterDto createNewsletterDto) {
        Newsletter newsletter = new Newsletter();
        newsletter.setName(createNewsletterDto.getName());
        return newsletter;
    }
}
