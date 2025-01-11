package fr.sidranie.newsther.dtos.news;

import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;

public class FullNewsDto {

    private Long id;
    private String title;
    private String content;
    private ShortNewsletterDto newsletter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ShortNewsletterDto getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(ShortNewsletterDto newsletter) {
        this.newsletter = newsletter;
    }
}
