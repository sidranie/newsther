package fr.sidranie.newsther.services.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import fr.sidranie.newsther.entities.News;
import fr.sidranie.newsther.repositories.NewsRepository;
import fr.sidranie.newsther.services.NewsService;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository repository;

    public NewsServiceImpl(NewsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Set<News> findNewsOfNewsletter(Long newsletterId) {
        return repository.findByNewsletterId(newsletterId);
    }

    @Override
    public Optional<News> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void createNews(News createNews) {
        repository.save(createNews);
    }
}
