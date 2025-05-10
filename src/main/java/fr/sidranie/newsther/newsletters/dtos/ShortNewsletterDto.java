package fr.sidranie.newsther.newsletters.dtos;

import fr.sidranie.newsther.people.dtos.ShortPersonDto;

public class ShortNewsletterDto {
    private Long id;
    private String title;
    private String slug;
    private ShortPersonDto creator;

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
}
