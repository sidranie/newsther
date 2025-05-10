package fr.sidranie.newsther.newsletters.dtos;

import java.util.List;

import fr.sidranie.newsther.news.dtos.ShortNewsDto;
import fr.sidranie.newsther.people.dtos.ShortPersonDto;
import fr.sidranie.newsther.subscriptions.dtos.ShortPersonSubscriptionDto;

public class FullNewsletterDto {
    private Long id;
    private String title;
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
