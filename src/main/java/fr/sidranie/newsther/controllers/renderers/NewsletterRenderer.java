package fr.sidranie.newsther.controllers.renderers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.sidranie.newsther.dtos.newsletter.CreateNewsletterDto;
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
        model.addAttribute("newsletters", service.findAll());
        return "listNewsletters";
    }

    @GetMapping("/create")
    public String createNewsletterForm() {
        return "createNewsletterForm";
    }

    @PostMapping("/create")
    public String createNewsletterAction(CreateNewsletterDto createNewsletterDto, Model model) {
        service.createNewsletter(NewsletterMapper.createNewsletterDtoToNewsletter(createNewsletterDto));
        return "redirect:/newsletters";
    }
}
