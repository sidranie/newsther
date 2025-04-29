package fr.sidranie.newsther.controllers.renderers;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import fr.sidranie.newsther.dtos.subscription.ShortNewsletterSubscriptionDto;
import fr.sidranie.newsther.mappers.SubscriptionMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.sidranie.newsther.dtos.person.CreatePersonDto;
import fr.sidranie.newsther.dtos.person.FullPersonDto;
import fr.sidranie.newsther.dtos.person.ShortPersonDto;
import fr.sidranie.newsther.entities.Person;
import fr.sidranie.newsther.mappers.PersonMapper;
import fr.sidranie.newsther.services.PersonService;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/people")
public class PersonRenderer {

    private PersonService service;

    public PersonRenderer(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public String listPeople(Model model) {
        List<ShortPersonDto> people = service.findAll()
                .stream()
                .map(PersonMapper::personToShortPersonDto)
                .sorted(Comparator.comparing(ShortPersonDto::getUsername))
                .toList();
        model.addAttribute("people", people);
        return "people/listPeople";
    }

    @GetMapping("/{username}")
    public String viewPerson(@PathVariable("username") String username, Model model) {
        FullPersonDto fullPersonDto = service.findByUsernameOrEmail(username)
                .map(PersonMapper::personToFullPersonDto)
                .orElseThrow(IllegalArgumentException::new);
        model.addAttribute("person", fullPersonDto);
        return "people/viewPerson";
    }

    @GetMapping("/me")
    public String viewMyProfile(Principal principal) throws IllegalAccessException {
        if (principal == null) {
            throw new IllegalAccessException();
        }
        return "redirect:/people/" + principal.getName();
    }

    @GetMapping("/create")
    public String createPersonForm() {
        return "people/createPersonForm";
    }

    @PostMapping("/create")
    public String createPerson(CreatePersonDto createPersonDto) {
        Person person = PersonMapper.createPersonDtoToPerson(createPersonDto);
        service.registerPerson(person);
        return "redirect:/login";
    }

    @PostMapping("/{id}/delete")
    @Transactional
    public String deletePerson(@PathVariable("id") Long id) {
        service.deleteById(id);
        return "redirect:/people";
    }

    @GetMapping("/{id}/subscriptions")
    public String listSubscriptionsForPerson(@PathVariable("id") Long id, Model model) {
        Person person = service.findById(id).orElseThrow(IllegalArgumentException::new);
        FullPersonDto personDto = PersonMapper.personToFullPersonDto(person);
        model.addAttribute("person", personDto);

        List<ShortNewsletterSubscriptionDto> subscriptions = person.getSubscriptions()
                .stream()
                .map(SubscriptionMapper::subscriptionToShortNewsletterSubscriptionDto)
                .sorted(Comparator.comparing(ShortNewsletterSubscriptionDto::getSince).reversed())
                .toList();
        model.addAttribute("subscriptions", subscriptions);
        return "people/listSubscriptionsForPerson";
    }
}
