package fr.sidranie.newsther.subscriptions.dtos;

import java.time.Instant;

public class ShortSubscriptionDto {
    private Long id;
    private Instant since;

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
}
