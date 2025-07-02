package fr.sidranie.newsther.news;

import java.time.Instant;

import fr.sidranie.newsther.newsletters.Newsletter;
import jakarta.persistence.*;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Instant creationDate;

    @Column(nullable = true)
    private Instant sendDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsletter_id", nullable = false)
    private Newsletter newsletter;

    public News() {
    }

    public News(String title, String content) {
        this.title = title;
        this.content = content;
    }

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

    public Instant getSendDate() {
        return sendDate;
    }

    public void setSendDate(Instant sendDate) {
        this.sendDate = sendDate;
    }

    public Newsletter getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(Newsletter newsletter) {
        this.newsletter = newsletter;
    }

    public boolean hasBeenSent() {
        return sendDate == null;
    }
}
