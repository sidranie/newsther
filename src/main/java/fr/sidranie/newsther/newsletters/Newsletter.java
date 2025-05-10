package fr.sidranie.newsther.newsletters;

import java.util.Set;

import fr.sidranie.newsther.news.News;
import fr.sidranie.newsther.people.Person;
import fr.sidranie.newsther.subscriptions.Subscription;
import jakarta.persistence.*;

@Entity
@Table(name = "newsletters")
public class Newsletter {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;

    @OneToMany(mappedBy = "newsletter", fetch = FetchType.LAZY)
    private Set<Subscription> subscriptions;

    @OneToMany(mappedBy = "newsletter", fetch = FetchType.LAZY)
    private Set<News> news;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<News> getNews() {
        return news;
    }

    public void setNews(Set<News> news) {
        this.news = news;
    }
}
