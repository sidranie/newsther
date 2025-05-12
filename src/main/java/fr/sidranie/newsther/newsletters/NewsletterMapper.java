package fr.sidranie.newsther.newsletters;

import fr.sidranie.newsther.newsletters.dtos.CreateNewsletterDto;

public class NewsletterMapper {

    public static Newsletter createNewsletterDtoToNewsletter(CreateNewsletterDto createNewsletterDto) {
        Newsletter newsletter = new Newsletter();
        newsletter.setTitle(createNewsletterDto.getTitle().trim());
        return newsletter;
    }
}
