package fr.sidranie.newsther.dtos.newsletter;

import java.util.List;

import fr.sidranie.newsther.dtos.news.ShortNewsDto;
import fr.sidranie.newsther.dtos.person.ShortPersonDto;
import fr.sidranie.newsther.dtos.subscription.ShortPersonSubscriptionDto;

public class FullNewsletterDto {
    private Long id;
    private String name;
    private String slug;
    private ShortPersonDto creator;
    private List<ShortNewsDto> newsList;
    private List<ShortPersonSubscriptionDto> subscriptions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ShortPersonDto getCreator() {
        return creator;
    }

    public void setCreator(ShortPersonDto creator) {
        this.creator = creator;
    }

    public List<ShortNewsDto> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<ShortNewsDto> newsList) {
        this.newsList = newsList;
    }

    public List<ShortPersonSubscriptionDto> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<ShortPersonSubscriptionDto> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
