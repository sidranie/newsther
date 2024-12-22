package fr.sidranie.newsther.dtos.subscription;

import java.time.Instant;

import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;

public class ShortNewsletterSubscriptionDto {
    private Long id;
    private Instant since;
    private ShortNewsletterDto newsletter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSince() {
        return since;
    }

    public void setSince(Instant since) {
        this.since = since;
    }

    public ShortNewsletterDto getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(ShortNewsletterDto newsletter) {
        this.newsletter = newsletter;
    }
}
