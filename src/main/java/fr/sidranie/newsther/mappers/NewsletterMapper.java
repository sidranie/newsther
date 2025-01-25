package fr.sidranie.newsther.mappers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import fr.sidranie.newsther.dtos.news.ShortNewsDto;
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
        shortNewsletterDto.setCreator(PersonMapper.personToShortPersonDto(newsletter.getCreator()));
        return shortNewsletterDto;
    }

    public static FullNewsletterDto newsletterToFullNewsletterDto(Newsletter newsletter) {
        FullNewsletterDto fullNewsletterDto = new FullNewsletterDto();
        fullNewsletterDto.setId(newsletter.getId());
        fullNewsletterDto.setName(newsletter.getName());
        fullNewsletterDto.setSlug(newsletter.getSlug());
        fullNewsletterDto.setCreator(PersonMapper.personToShortPersonDto(newsletter.getCreator()));

        List<ShortNewsDto> newsList = newsletter.getNews()
                .stream()
                .map(NewsMapper::newsToShortNewsDto)
                .sorted(Comparator.comparing(ShortNewsDto::getCreationDate).reversed())
                .toList();
        fullNewsletterDto.setNewsList(newsList);

        List<ShortPersonSubscriptionDto> subscriptions = newsletter.getSubscriptions()
                .stream()
                .map(SubscriptionMapper::subscriptionToShortPersonSubscriptionDto)
                .sorted(Comparator.comparing(ShortPersonSubscriptionDto::getId))
                .collect(Collectors.toList());
        fullNewsletterDto.setSubscriptions(subscriptions);

        return fullNewsletterDto;
    }

    public static Newsletter createNewsletterDtoToNewsletter(CreateNewsletterDto createNewsletterDto) {
        Newsletter newsletter = new Newsletter();
        newsletter.setName(createNewsletterDto.getName());
        return newsletter;
    }
}
