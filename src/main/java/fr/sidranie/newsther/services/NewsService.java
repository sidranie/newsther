package fr.sidranie.newsther.services;

import java.util.Optional;
import java.util.Set;

import fr.sidranie.newsther.entities.News;

public interface NewsService {

    Set<News> findNewsOfNewsletter(Long newsletterId);
    Optional<News> findById(Long id);
    void createNews(News createNews);
}
