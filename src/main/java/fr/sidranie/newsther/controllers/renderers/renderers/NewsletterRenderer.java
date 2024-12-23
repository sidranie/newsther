package fr.sidranie.newsther.controllers.renderers.renderers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.sidranie.newsther.services.NewsletterService;

@Controller
public class NewsletterRenderer {

    private final NewsletterService service;

    public NewsletterRenderer(NewsletterService service) {
        this.service = service;
    }

    @RequestMapping("/newsletters")
    public String allNewsletters() {
        
        return "listNewsletters";
    }
}
