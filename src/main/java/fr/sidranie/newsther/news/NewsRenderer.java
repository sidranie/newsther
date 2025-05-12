package fr.sidranie.newsther.news;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.sidranie.newsther.news.dtos.CreateNewsDto;
import fr.sidranie.newsther.news.dtos.EditNewsDto;
import fr.sidranie.newsther.newsletters.Newsletter;
import fr.sidranie.newsther.newsletters.NewsletterRepository;

@Controller
@RequestMapping("/news")
public class NewsRenderer {

    private final NewsService service;
    private final NewsRepository repository;
    private final NewsletterRepository newsletterRepository;

    public NewsRenderer(NewsService service,
                        NewsRepository newsRepository,
                        NewsletterRepository newsletterRepository) {
        this.service = service;
        this.repository = newsRepository;
        this.newsletterRepository = newsletterRepository;
    }

    @GetMapping
    public String renderNewsList(@RequestParam Long newsletterId, Model model) {
        if (newsletterId == null) {
            throw new IllegalArgumentException();
        }

        List<News> newsList = repository.findByNewsletterId(newsletterId)
                .stream()
                .sorted(Comparator.comparing(News::getTitle))
                .toList();
        model.addAttribute("newsList", newsList);
        return "news/listNews";
    }

    @GetMapping("/{id}")
    public String renderNewsDetails(@PathVariable("id") Long id, Model model) {
        News news = repository.findById(id).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("news", news);
        return "news/viewNews";
    }

    @GetMapping("/create")
    public String renderNewsCreationForm(@RequestParam Long newsletterId, Model model) {
        if (newsletterId == null) {
            throw new IllegalArgumentException();
        }

        Newsletter newsletter = newsletterRepository.findById(newsletterId)
                .orElseThrow(IllegalArgumentException::new);
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

    @GetMapping("/{id}/edit")
    public String renderNewsEditionForm(@PathVariable("id") Long id, Principal principal, Model model) {
        News news = repository.findById(id)
                .filter(foundNews -> foundNews.getSendDate() == null)
                .orElseThrow(IllegalArgumentException::new);

        if (!news.getNewsletter().getCreator().getUsername().equals(principal.getName())) {
            throw new IllegalAccessError();
        }

        model.addAttribute("news", news);
        return "news/editNews";
    }

    @PostMapping("/{id}/edit")
    public String performNewsEdition(@PathVariable("id") Long id, EditNewsDto editNewsDto, Principal principal, Model model) {
        News news = repository.findById(id)
                .filter(foundNews -> foundNews.getSendDate() == null)
                .orElseThrow(IllegalArgumentException::new);

        if (!news.getNewsletter().getCreator().getUsername().equals(principal.getName())) {
            throw new IllegalAccessError();
        }

        News newsUpdates = new News();
        newsUpdates.setTitle(editNewsDto.getTitle());
        newsUpdates.setContent(editNewsDto.getContent());

        News result = service.updateNews(news, newsUpdates);

        return "redirect:/news/" + result.getId();
    }
}
