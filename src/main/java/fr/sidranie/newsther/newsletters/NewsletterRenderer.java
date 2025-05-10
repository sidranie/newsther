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
import fr.sidranie.newsther.newsletters.dtos.FullNewsletterDto;
import fr.sidranie.newsther.newsletters.dtos.ShortNewsletterDto;

@Controller
@RequestMapping("/newsletters")
public class NewsletterRenderer {

    private final NewsletterService service;
    private final NewsletterRepository repository;

    public NewsletterRenderer(NewsletterService service, NewsletterRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    public String allNewsletters(Model model) {
        List<ShortNewsletterDto> newsletters = repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Newsletter::getTitle))
                .map(NewsletterMapper::newsletterToShortNewsletterDto)
                .toList();
        model.addAttribute("newsletters", newsletters);
        return "newsletters/listNewsletters";
    }

    @GetMapping("/{slug}")
    public String viewNewsletter(@PathVariable("slug") String slug, Model model) {
        FullNewsletterDto fullNewsletterDto = repository.findBySlug(slug)
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
    public String createNewsletterAction(CreateNewsletterDto createNewsletterDto,
                                         Principal principal) throws IllegalAccessException {
        if (principal == null) {
            throw new IllegalAccessException();
        }

        Newsletter newsletter = NewsletterMapper.createNewsletterDtoToNewsletter(createNewsletterDto);

        service.createNewsletter(newsletter, principal);
        return "redirect:/newsletters";
    }

    @GetMapping("/{slug}/edit")
    public String editNewsletterForm(@PathVariable("slug") String slug, Principal principal, Model model) {
        FullNewsletterDto fullNewsletterDto = repository.findBySlug(slug)
                .filter(newsletter -> newsletter.getCreator().getUsername().equals(principal.getName()))
                .map(NewsletterMapper::newsletterToFullNewsletterDto)
                .orElseThrow(IllegalAccessError::new);
        model.addAttribute("newsletter", fullNewsletterDto);
        return "newsletters/editNewsletter";
    }

    @PostMapping("/{slug}/edit")
    public String editNewsletter(@PathVariable("slug") String slug,
                                 EditNewsletterDto editNewsletterDto,
                                 Principal principal) throws IllegalAccessException {
        if (principal == null) {
            throw new IllegalAccessException();
        }

        Newsletter newsletter = repository.findBySlug(slug).orElseThrow(IllegalAccessError::new);

        if (!newsletter.getCreator().getUsername().equals(principal.getName())) {
            throw new IllegalAccessError();
        }

        Newsletter newsletterUpdates = new Newsletter();
        newsletterUpdates.setTitle(editNewsletterDto.getTitle());

        Newsletter result = service.editNewsletter(newsletter, newsletterUpdates);

        return "redirect:/newsletters/" + result.getSlug();
    }

    @PostMapping("/{id}/delete")
    public String deleteNewsletter(@PathVariable("id") Long id) {
        service.deleteNewsletter(id);
        return "redirect:/newsletters";
    }
}
