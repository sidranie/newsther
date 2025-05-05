package fr.sidranie.newsther.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fr.sidranie.newsther.entities.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Set<News> findByNewsletterId(Long newsletterId);

    void deleteByNewsletterId(Long newsletterId);
}
