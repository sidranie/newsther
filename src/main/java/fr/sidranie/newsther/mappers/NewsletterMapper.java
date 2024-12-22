package fr.sidranie.newsther.mappers;

import fr.sidranie.newsther.dtos.newsletter.CreateNewsletterDto;
import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;
import fr.sidranie.newsther.entities.Newsletter;

public class NewsletterMapper {

    public static ShortNewsletterDto newsletterToShortNewsletterDto(Newsletter newsletter) {
        ShortNewsletterDto shortNewsletterDto = new ShortNewsletterDto();
        shortNewsletterDto.setId(newsletter.getId());
        shortNewsletterDto.setName(newsletter.getName());
        return shortNewsletterDto;
    }

    public static Newsletter createNewsletterDtoToNewsletter(CreateNewsletterDto createNewsletterDto) {
        Newsletter newsletter = new Newsletter();
        newsletter.setName(createNewsletterDto.getName());
        return newsletter;
    }
}
