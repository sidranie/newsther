package fr.sidranie.newsther.mappers;

import fr.sidranie.newsther.dtos.news.CreateNewsDto;
import fr.sidranie.newsther.dtos.news.FullNewsDto;
import fr.sidranie.newsther.dtos.news.ShortNewsDto;
import fr.sidranie.newsther.entities.News;

public class NewsMapper {

    public static ShortNewsDto newsToShortNewsDto(News news) {
        ShortNewsDto shortNews = new ShortNewsDto();
        shortNews.setId(news.getId());
        shortNews.setTitle(news.getTitle());
        shortNews.setContent(news.getContent());
        shortNews.setCreationDate(news.getCreationDate());
        return shortNews;
    }

    public static FullNewsDto newsToFullNewsDto(News news) {
        FullNewsDto fullNews = new FullNewsDto();
        fullNews.setId(news.getId());
        fullNews.setTitle(news.getTitle());
        fullNews.setContent(news.getContent());
        fullNews.setCreationDate(news.getCreationDate());
        fullNews.setNewsletter(NewsletterMapper.newsletterToShortNewsletterDto(news.getNewsletter()));
        return fullNews;
    }

    public static News createNewsDtoToNews(CreateNewsDto createNewsDto) {
        News news = new News();
        news.setTitle(createNewsDto.getTitle().trim());
        news.setContent(createNewsDto.getContent().trim());
        return news;
    }
}
