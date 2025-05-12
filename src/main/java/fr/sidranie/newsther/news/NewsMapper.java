package fr.sidranie.newsther.news;

import fr.sidranie.newsther.news.dtos.CreateNewsDto;

public class NewsMapper {

    public static News createNewsDtoToNews(CreateNewsDto createNewsDto) {
        News news = new News();
        news.setTitle(createNewsDto.getTitle().trim());
        news.setContent(createNewsDto.getContent().trim());
        return news;
    }
}
