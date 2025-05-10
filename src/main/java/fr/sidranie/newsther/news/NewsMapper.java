package fr.sidranie.newsther.news;

import fr.sidranie.newsther.news.dtos.CreateNewsDto;
import fr.sidranie.newsther.news.dtos.FullNewsDto;
import fr.sidranie.newsther.news.dtos.ShortNewsDto;
import fr.sidranie.newsther.newsletters.NewsletterMapper;

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
