package fr.sidranie.newsther.news;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class NewsService {

    private final NewsRepository repository;

    public NewsService(NewsRepository repository) {
        this.repository = repository;
    }

    public Set<News> findNewsOfNewsletter(Long newsletterId) {
        return repository.findByNewsletterId(newsletterId);
    }

    public Optional<News> findById(Long id) {
        return repository.findById(id);
    }

    public void createNews(News createNews) {
        createNews.setCreationDate(Instant.now());
        repository.save(createNews);
    }

    public News updateNews(News news, News newsUpdates) {
        news.setTitle(newsUpdates.getTitle());
        news.setContent(newsUpdates.getContent());
        repository.save(news);
        return news;
    }
}
