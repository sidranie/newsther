package fr.sidranie.newsther.subscriptions;

import java.time.Instant;

import fr.sidranie.newsther.newsletters.Newsletter;
import fr.sidranie.newsther.people.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "subscriptions",
        uniqueConstraints =
        @UniqueConstraint(name = "unique_subscription_person_newsletter",
                columnNames = {"person_id", "newsletter_id"}))
public class Subscription {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant since;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsletter_id", nullable = false)
    private Newsletter newsletter;

    public Subscription() {
    }

    public Subscription(Instant since, Person person, Newsletter newsletter) {
        this.since = since;
        this.person = person;
        this.newsletter = newsletter;
    }

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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Newsletter getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(Newsletter newsletter) {
        this.newsletter = newsletter;
    }
}
