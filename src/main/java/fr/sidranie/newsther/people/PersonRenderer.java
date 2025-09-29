package fr.sidranie.newsther.people;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import fr.sidranie.newsther.people.dtos.CreatePersonDto;
import fr.sidranie.newsther.people.dtos.EditPersonDto;
import fr.sidranie.newsther.subscriptions.Subscription;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/people")
public class PersonRenderer {

    private PersonService service;
    private People people;

    public PersonRenderer(PersonService service, People people) {
        this.service = service;
        this.people = people;
    }

    @GetMapping
    public String renderPeopleList(Model model) {
        List<Person> sortedPeople = people.findAll()
                .stream()
                .sorted(Comparator.comparing(Person::getUsername))
                .toList();
        model.addAttribute("people", sortedPeople);
        return "people/listPeople";
    }

    @GetMapping("/{username}")
    public String renderPersonDetails(@PathVariable("username") String username, Model model) {
        Person person = people.findByUsernameOrEmail(username, username)
                .orElseThrow(IllegalArgumentException::new);
        model.addAttribute("person", person);
        return "people/viewPerson";
    }

    @GetMapping("/me")
    public String renderMyProfile(Principal principal) throws IllegalAccessException {
        if (principal == null) {
            throw new IllegalAccessException();
        }
        return "redirect:/people/" + principal.getName();
    }

    @GetMapping("/create")
    public String renderPersonCreationForm(Model model) {
        model.addAttribute("createPersonDto", new CreatePersonDto());
        return "people/createPersonForm";
    }

    @PostMapping("/create")
    public String performPersonCreation(CreatePersonDto createPersonDto, Model model) {
        Person person = PersonMapper.createPersonDtoToPerson(createPersonDto);
        try {
            service.registerPerson(person);
        } catch (IllegalArgumentException e) {
            model.addAttribute("createPersonDto", createPersonDto);
            model.addAttribute("error", e.getMessage());
            return "people/createPersonForm";
        }
        return "redirect:/login";
    }

    @GetMapping("/{id}/edit")
    public String renderPersonEditionForm(@PathVariable("id") Long id, Principal principal, Model model) {
        Person person = people.findById(id).orElseThrow(IllegalArgumentException::new);

        if (!person.getUsername().equals(principal.getName())) {
            throw new IllegalAccessError("You cannot edit this person.");
        }

        model.addAttribute("person", person);
        return "people/editPerson";
    }

    @PostMapping("/{id}/edit")
    public String performPersonEdition(@PathVariable("id") Long id, EditPersonDto editPersonDto, Principal principal) {
        Person person = people.findById(id).orElseThrow(IllegalArgumentException::new);

        if (!person.getUsername().equals(principal.getName())) {
            throw new IllegalAccessError("You cannot edit this person.");
        }

        Person personUpdates = new Person(
                editPersonDto.getUsername(),
                editPersonDto.getEmail(),
                editPersonDto.getPassword(),
                editPersonDto.getGivenName(),
                editPersonDto.getFamilyName());

        service.updatePerson(person, personUpdates);
        return "redirect:/perform_logout";
    }

    @PostMapping("/{id}/delete")
    @Transactional
    public String performPersonDeletion(@PathVariable("id") Long id) {
        service.deleteById(id);
        return "redirect:/people";
    }

    @GetMapping("/{id}/subscriptions")
    public String renderPersonSubscriptionList(@PathVariable("id") Long id, Model model) {
        Person person = people.findById(id).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("person", person);

        List<Subscription> subscriptions = person.getSubscriptions()
                .stream()
                .sorted(Comparator.comparing(Subscription::getSince).reversed())
                .toList();
        model.addAttribute("subscriptions", subscriptions);
        return "people/listSubscriptionsForPerson";
    }
}
