package fr.sidranie.newsther.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.sidranie.newsther.entities.News;

@Repository
public interface NewsRepository extends CrudRepository<News, Long> {

    Set<News> findByNewsletterId(Long newsletterId);
}
