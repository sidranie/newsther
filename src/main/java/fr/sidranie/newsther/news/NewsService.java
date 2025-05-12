package fr.sidranie.newsther.news;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class NewsService {

    private final Newses newses;

    public NewsService(Newses newses) {
        this.newses = newses;
    }

    public Set<News> findNewsOfNewsletter(Long newsletterId) {
        return newses.findByNewsletterId(newsletterId);
    }

    public Optional<News> findById(Long id) {
        return newses.findById(id);
    }

    public void createNews(News createNews) {
        createNews.setCreationDate(Instant.now());
        newses.save(createNews);
    }

    public News updateNews(News news, News newsUpdates) {
        news.setTitle(newsUpdates.getTitle());
        news.setContent(newsUpdates.getContent());
        newses.save(news);
        return news;
    }
}
