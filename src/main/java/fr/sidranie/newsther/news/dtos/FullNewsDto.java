package fr.sidranie.newsther.news.dtos;

import java.time.Instant;

import fr.sidranie.newsther.newsletters.dtos.ShortNewsletterDto;

public class FullNewsDto {

    private Long id;
    private String title;
    private String content;
    private Instant creationDate;
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

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public ShortNewsletterDto getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(ShortNewsletterDto newsletter) {
        this.newsletter = newsletter;
    }
}
