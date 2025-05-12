package fr.sidranie.newsther.newsletters;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import fr.sidranie.newsther.newsletters.dtos.CreateNewsletterDto;
import fr.sidranie.newsther.newsletters.dtos.EditNewsletterDto;

@Controller
@RequestMapping("/newsletters")
public class NewsletterRenderer {

    private final NewsletterService service;
    private final Newsletters newsletters;

    public NewsletterRenderer(NewsletterService service, Newsletters newsletters) {
        this.service = service;
        this.newsletters = newsletters;
    }

    @GetMapping
    public String renderNewslettersList(Model model) {
        List<Newsletter> sortedNewsletters = this.newsletters.findAll()
                .stream()
                .sorted(Comparator.comparing(Newsletter::getTitle))
                .toList();
        model.addAttribute("newsletters", sortedNewsletters);
        return "newsletters/listNewsletters";
    }

    @GetMapping("/{slug}")
    public String renderNewsletterDetails(@PathVariable("slug") String slug, Model model) {
        Newsletter newsletter = newsletters.findBySlug(slug).orElseThrow(IllegalAccessError::new);
        model.addAttribute("newsletter", newsletter);
        return "newsletters/viewNewsletter";
    }

    @GetMapping("/create")
    public String renderNewsletterCreationForm() {
        return "newsletters/createNewsletterForm";
    }

    @PostMapping("/create")
    public String performNewsletterCreation(CreateNewsletterDto createNewsletterDto,
                                            Principal principal) throws IllegalAccessException {
        if (principal == null) {
            throw new IllegalAccessException();
        }

        Newsletter newsletter = NewsletterMapper.createNewsletterDtoToNewsletter(createNewsletterDto);

        service.createNewsletter(newsletter, principal);
        return "redirect:/newsletters";
    }

    @GetMapping("/{slug}/edit")
    public String renderNewsletterEditionForm(@PathVariable("slug") String slug, Principal principal, Model model) {
        Newsletter newsletter = newsletters.findBySlug(slug)
                .filter(foundNewsletter -> foundNewsletter.getCreator().getUsername().equals(principal.getName()))
                .orElseThrow(IllegalAccessError::new);
        model.addAttribute("newsletter", newsletter);
        return "newsletters/editNewsletter";
    }

    @PostMapping("/{slug}/edit")
    public String performNewsletterEdition(@PathVariable("slug") String slug,
                                           EditNewsletterDto editNewsletterDto,
                                           Principal principal) throws IllegalAccessException {
        if (principal == null) {
            throw new IllegalAccessException();
        }

        Newsletter newsletter = newsletters.findBySlug(slug).orElseThrow(IllegalAccessError::new);

        if (!newsletter.getCreator().getUsername().equals(principal.getName())) {
            throw new IllegalAccessError();
        }

        Newsletter newsletterUpdates = new Newsletter();
        newsletterUpdates.setTitle(editNewsletterDto.getTitle());

        Newsletter result = service.editNewsletter(newsletter, newsletterUpdates);

        return "redirect:/newsletters/" + result.getSlug();
    }

    @PostMapping("/{id}/delete")
    public String performNewsletterDeletion(@PathVariable("id") Long id) {
        service.deleteNewsletter(id);
        return "redirect:/newsletters";
    }
}
