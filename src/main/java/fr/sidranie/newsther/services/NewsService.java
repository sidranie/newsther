package fr.sidranie.newsther.services;

import fr.sidranie.newsther.entities.News;

public interface NewsService {
    void createNews(News news);

    void deleteNews(News news);
}
