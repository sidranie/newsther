package fr.sidranie.newsther.controllers.renderers;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.sidranie.newsther.dtos.newsletter.CreateNewsletterDto;
import fr.sidranie.newsther.dtos.newsletter.FullNewsletterDto;
import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;
import fr.sidranie.newsther.entities.Newsletter;
import fr.sidranie.newsther.mappers.NewsletterMapper;
import fr.sidranie.newsther.services.NewsletterService;

@Controller
@RequestMapping("/newsletters")
public class NewsletterRenderer {

    private final NewsletterService service;

    public NewsletterRenderer(NewsletterService service) {
        this.service = service;
    }

    @GetMapping
    public String allNewsletters(Model model) {
        List<ShortNewsletterDto> newsletters = service.findAll()
                .stream()
                .sorted(Comparator.comparing(Newsletter::getName))
                .map(NewsletterMapper::newsletterToShortNewsletterDto)
                .toList();
        model.addAttribute("newsletters", newsletters);
        return "listNewsletters";
    }

    @GetMapping("/{id}")
    public String viewNewsletter(@PathVariable("id") Long id, Model model) {
        FullNewsletterDto fullNewsletterDto = service.findById(id)
                .map(NewsletterMapper::newsletterToFullNewsletterDto)
                .orElseThrow(IllegalAccessError::new);
        model.addAttribute("newsletter", fullNewsletterDto);
        return "viewNewsletter";
    }

    @GetMapping("/create")
    public String createNewsletterForm() {
        return "createNewsletterForm";
    }

    @PostMapping("/create")
    public String createNewsletterAction(CreateNewsletterDto createNewsletterDto) {
        service.createNewsletter(NewsletterMapper.createNewsletterDtoToNewsletter(createNewsletterDto));
        return "redirect:/newsletters";
    }
}
