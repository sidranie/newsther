package fr.sidranie.newsther.controllers.renderers;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.sidranie.newsther.dtos.news.CreateNewsDto;
import fr.sidranie.newsther.dtos.news.FullNewsDto;
import fr.sidranie.newsther.dtos.news.ShortNewsDto;
import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;
import fr.sidranie.newsther.entities.News;
import fr.sidranie.newsther.entities.Newsletter;
import fr.sidranie.newsther.mappers.NewsMapper;
import fr.sidranie.newsther.mappers.NewsletterMapper;
import fr.sidranie.newsther.repositories.NewsletterRepository;
import fr.sidranie.newsther.services.NewsService;

@Controller
@RequestMapping("/news")
public class NewsRenderer {

    private final NewsService service;
    private final NewsletterRepository newsletterRepository;

    public NewsRenderer(NewsService service, NewsletterRepository newsletterRepository) {
        this.service = service;
        this.newsletterRepository = newsletterRepository;
    }

    @GetMapping
    public String listNewsOfNewsletter(@RequestParam Long newsletterId, Model model) {
        if (newsletterId == null) {
            throw new IllegalArgumentException();
        }

        Set<News> newsList = service.findNewsOfNewsletter(newsletterId);
        List<ShortNewsDto> newsDtoList = newsList.stream()
                .map(NewsMapper::newsToShortNewsDto)
                .sorted(Comparator.comparing(ShortNewsDto::getTitle))
                .toList();
        model.addAttribute("newsList", newsDtoList);
        return "news/listNews";
    }

    @GetMapping("/{id}")
    public String viewNews(@PathVariable("id") Long id, Model model) {
        FullNewsDto newsDto = NewsMapper.newsToFullNewsDto(service.findById(id)
                .orElseThrow(IllegalArgumentException::new));
        model.addAttribute("news", newsDto);
        return "news/viewNews";
    }

    @GetMapping("/create")
    public String renderNewsCreationForm(@RequestParam Long newsletterId, Model model) {
        if (newsletterId == null) {
            throw new IllegalArgumentException();
        }

        ShortNewsletterDto newsletter = NewsletterMapper.newsletterToShortNewsletterDto(
                newsletterRepository.findById(newsletterId).orElseThrow(IllegalArgumentException::new));
        model.addAttribute("newsletter", newsletter);
        return "news/createNews";
    }

    @PostMapping
    public String performNewsCreation(CreateNewsDto createNewsDto) {
        Newsletter newsletter = newsletterRepository.findById(createNewsDto.getNewsletterId())
                .orElseThrow(IllegalArgumentException::new);
        News news = NewsMapper.createNewsDtoToNews(createNewsDto);
        news.setNewsletter(newsletter);
        service.createNews(news);
        return "redirect:news/" + news.getId();
    }
}
