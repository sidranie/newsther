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
import fr.sidranie.newsther.newsletters.Newsletters;

@Controller
@RequestMapping("/news")
public class NewsRenderer {

    private final NewsService service;
    private final Newses newses;
    private final Newsletters newsletters;

    public NewsRenderer(NewsService service,
                        Newses newses,
                        Newsletters newsletters) {
        this.service = service;
        this.newses = newses;
        this.newsletters = newsletters;
    }

    @GetMapping
    public String renderNewsList(@RequestParam Long newsletterId, Model model) {
        if (newsletterId == null) {
            throw new IllegalArgumentException();
        }

        List<News> sortedNewses = newses.findByNewsletterId(newsletterId)
                .stream()
                .sorted(Comparator.comparing(News::getTitle))
                .toList();
        model.addAttribute("newsList", sortedNewses);
        return "news/listNews";
    }

    @GetMapping("/{id}")
    public String renderNewsDetails(@PathVariable("id") Long id, Model model) {
        News news = newses.findById(id).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("news", news);
        return "news/viewNews";
    }

    @GetMapping("/create")
    public String renderNewsCreationForm(@RequestParam Long newsletterId, Model model) {
        if (newsletterId == null) {
            throw new IllegalArgumentException();
        }

        Newsletter newsletter = newsletters.findById(newsletterId)
                .orElseThrow(IllegalArgumentException::new);
        model.addAttribute("newsletter", newsletter);
        return "news/createNews";
    }

    @PostMapping
    public String performNewsCreation(CreateNewsDto createNewsDto) {
        Newsletter newsletter = newsletters.findById(createNewsDto.getNewsletterId())
                .orElseThrow(IllegalArgumentException::new);
        News news = NewsMapper.createNewsDtoToNews(createNewsDto);
        news.setNewsletter(newsletter);
        service.createNews(news);
        return "redirect:news/" + news.getId();
    }

    @GetMapping("/{id}/edit")
    public String renderNewsEditionForm(@PathVariable("id") Long id, Principal principal, Model model) {
        News news = newses.findById(id)
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
        News news = newses.findById(id)
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
