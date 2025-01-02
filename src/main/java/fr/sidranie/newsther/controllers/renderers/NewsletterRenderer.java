package fr.sidranie.newsther.controllers.renderers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.sidranie.newsther.services.NewsletterService;

@Controller
@RequestMapping("/newsletters")
public class NewsletterRenderer {

    private final NewsletterService service;

    public NewsletterRenderer(NewsletterService service) {
        this.service = service;
    }

    @RequestMapping
    public String allNewsletters(Model model) {
        model.addAttribute("newsletters", service.findAll());
        return "listNewsletters";
    }
}
