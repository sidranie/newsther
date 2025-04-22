package fr.sidranie.newsther.controllers.renderers;

import java.security.Principal;
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
import fr.sidranie.newsther.services.PersonService;

@Controller
@RequestMapping("/newsletters")
public class NewsletterRenderer {

    private final NewsletterService service;
    private final PersonService personService;

    public NewsletterRenderer(NewsletterService service,
                              PersonService personService) {
        this.service = service;
        this.personService = personService;
    }

    @GetMapping
    public String allNewsletters(Model model) {
        List<ShortNewsletterDto> newsletters = service.findAll()
                .stream()
                .sorted(Comparator.comparing(Newsletter::getTitle))
                .map(NewsletterMapper::newsletterToShortNewsletterDto)
                .toList();
        model.addAttribute("newsletters", newsletters);
        return "newsletters/listNewsletters";
    }

    @GetMapping("/{slug}")
    public String viewNewsletter(@PathVariable("slug") String slug, Model model) {
        FullNewsletterDto fullNewsletterDto = service.findBySlug(slug)
                .map(NewsletterMapper::newsletterToFullNewsletterDto)
                .orElseThrow(IllegalAccessError::new);
        model.addAttribute("newsletter", fullNewsletterDto);
        return "newsletters/viewNewsletter";
    }

    @GetMapping("/create")
    public String createNewsletterForm() {
        return "newsletters/createNewsletterForm";
    }

    @PostMapping("/create")
    public String createNewsletterAction(CreateNewsletterDto createNewsletterDto, Principal principal) throws IllegalAccessException {
        if (principal == null) {
            throw new IllegalAccessException();
        }

        Newsletter newsletter = NewsletterMapper.createNewsletterDtoToNewsletter(createNewsletterDto);

        service.createNewsletter(newsletter, principal);
        return "redirect:/newsletters";
    }

    @PostMapping("/{id}/delete")
    public String deleteNewsletter(@PathVariable("id") Long id) {
        service.deleteNewsletter(id);
        return "redirect:/newsletters";
    }
}
