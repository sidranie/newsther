package fr.sidranie.newsther.controllers.renderers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

import fr.sidranie.newsther.services.SubscriptionService;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionRenderer {

    private final SubscriptionService service;

    public SubscriptionRenderer(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping("/{newsletterSlug}/subscribe")
    public String subscribe(@PathVariable("newsletterSlug") String slug, Principal principal, HttpServletRequest request) throws IllegalAccessException {
        service.subscribePersonToNewsletter(principal, slug);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/{newsletterSlug}/unsubscribe")
    public String unsubscribe(@PathVariable("newsletterSlug") String slug, Principal principal, HttpServletRequest request) throws IllegalAccessException {
        service.unsubscribePersonFromNewsletter(principal, slug);
        return "redirect:" + request.getHeader("Referer");
    }
}
