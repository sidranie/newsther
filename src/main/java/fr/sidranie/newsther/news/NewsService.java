package fr.sidranie.newsther.news;

import java.time.Instant;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.sidranie.newsther.newsletters.Newsletters;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    private final Newses newses;
    private final Newsletters newsletters;

    public NewsService(Newses newses, Newsletters newsletters) {
        this.newses = newses;
        this.newsletters = newsletters;
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

    public Map<Long, News> findNewsesToSend() {
        return this.newsletters.findAll()
                .stream()
                .filter(newsletter -> !newsletter.getNews().isEmpty())
                .map(newsletter -> { // Get newses to send. Identified by their newsletter id
                    List<News> notSentNewses = newsletter.getNews()
                            .stream()
                            .filter(news -> Objects.isNull(news.getSendDate()))
                            .sorted(Comparator.comparing(News::getCreationDate))
                            .toList();
                    return notSentNewses.isEmpty() ? null :
                            new AbstractMap.SimpleEntry<>(newsletter.getId(), notSentNewses.getFirst());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
