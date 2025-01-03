package fr.sidranie.newsther.controllers.renderers;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;
import fr.sidranie.newsther.dtos.subscription.ShortNewsletterSubscriptionDto;
import fr.sidranie.newsther.entities.Subscription;
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

    @GetMapping("/{id}")
    public String viewPerson(@PathVariable("id") Long id, Model model) {
        FullPersonDto fullPersonDto = service.findById(id)
                .map(PersonMapper::personToFullPersonDto)
                .orElseThrow(IllegalArgumentException::new);
        model.addAttribute("person", fullPersonDto);
        return "people/viewPerson";
    }

    @GetMapping("/create")
    public String createPersonForm() {
        return "people/createPersonForm";
    }

    @PostMapping("/create")
    public String createPerson(CreatePersonDto createPersonDto) {
        Person person = PersonMapper.createPersonDtoToPerson(createPersonDto);
        service.registerPerson(person);
        return "redirect:/people";
    }

    @PostMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id") Long id) {
        service.deleteById(id);
        return "redirect:/people";
    }

    @GetMapping("/{id}/subscriptions")
    public String listSubscriptionsForPerson(@PathVariable("id") Long id, Model model) {
        Person person = service.findById(id).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("person", PersonMapper.personToShortPersonDto(person));

        List<ShortNewsletterSubscriptionDto> subscriptions = person.getSubscriptions()
                .stream()
                .map(SubscriptionMapper::subscriptionToShortNewsletterSubscriptionDto)
                .sorted(Comparator.comparing(ShortNewsletterSubscriptionDto::getSince).reversed())
                .toList();
        model.addAttribute("subscriptions", subscriptions);
        return "people/listSubscriptionsForPerson";
    }
}
