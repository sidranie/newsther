package fr.sidranie.newsther.news;

import fr.sidranie.newsther.news.dtos.CreateNewsDto;

public class NewsMapper {

    public static News createNewsDtoToNews(CreateNewsDto createNewsDto) {
        return new News(createNewsDto.getTitle().trim(), createNewsDto.getContent().trim());
    }
}
