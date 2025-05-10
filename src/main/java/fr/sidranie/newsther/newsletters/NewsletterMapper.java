package fr.sidranie.newsther.newsletters;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import fr.sidranie.newsther.news.NewsMapper;
import fr.sidranie.newsther.news.dtos.ShortNewsDto;
import fr.sidranie.newsther.newsletters.dtos.CreateNewsletterDto;
import fr.sidranie.newsther.newsletters.dtos.FullNewsletterDto;
import fr.sidranie.newsther.newsletters.dtos.ShortNewsletterDto;
import fr.sidranie.newsther.people.PersonMapper;
import fr.sidranie.newsther.subscriptions.SubscriptionMapper;
import fr.sidranie.newsther.subscriptions.dtos.ShortPersonSubscriptionDto;

public class NewsletterMapper {

    public static ShortNewsletterDto newsletterToShortNewsletterDto(Newsletter newsletter) {
        ShortNewsletterDto shortNewsletterDto = new ShortNewsletterDto();
        shortNewsletterDto.setId(newsletter.getId());
        shortNewsletterDto.setTitle(newsletter.getTitle());
        shortNewsletterDto.setSlug(newsletter.getSlug());
        shortNewsletterDto.setCreator(PersonMapper.personToShortPersonDto(newsletter.getCreator()));
        return shortNewsletterDto;
    }

    public static FullNewsletterDto newsletterToFullNewsletterDto(Newsletter newsletter) {
        FullNewsletterDto fullNewsletterDto = new FullNewsletterDto();
        fullNewsletterDto.setId(newsletter.getId());
        fullNewsletterDto.setTitle(newsletter.getTitle());
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
        newsletter.setTitle(createNewsletterDto.getTitle().trim());
        return newsletter;
    }
}
