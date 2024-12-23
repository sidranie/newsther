package fr.sidranie.newsther.dtos.subscription;

import java.time.Instant;

import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;
import fr.sidranie.newsther.dtos.person.ShortPersonDto;

public class FullSubscriptionDto {
    private Long id;
    private Instant since;
    private ShortPersonDto person;
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

    public ShortPersonDto getPerson() {
        return person;
    }

    public void setPerson(ShortPersonDto person) {
        this.person = person;
    }

    public ShortNewsletterDto getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(ShortNewsletterDto newsletter) {
        this.newsletter = newsletter;
    }
}
