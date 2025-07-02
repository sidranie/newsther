package fr.sidranie.newsther.newsletters;

import fr.sidranie.newsther.newsletters.dtos.CreateNewsletterDto;

public class NewsletterMapper {

    public static Newsletter createNewsletterDtoToNewsletter(CreateNewsletterDto createNewsletterDto) {
        return new Newsletter(createNewsletterDto.getTitle().trim());
    }
}
