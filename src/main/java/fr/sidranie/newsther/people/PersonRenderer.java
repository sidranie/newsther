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
        List<Person> sortedPeople = service.findAll()
                .stream()
                .sorted(Comparator.comparing(Person::getUsername))
                .toList();
        model.addAttribute("people", sortedPeople);
        return "people/listPeople";
    }

    @GetMapping("/{username}")
    public String renderPersonDetails(@PathVariable("username") String username, Model model) {
        Person person = service.findByUsernameOrEmail(username)
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
    public String renderPersonCreationForm() {
        return "people/createPersonForm";
    }

    @PostMapping("/create")
    public String performPersonCreation(CreatePersonDto createPersonDto) {
        Person person = PersonMapper.createPersonDtoToPerson(createPersonDto);
        service.registerPerson(person);
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

        Person personUpdates = new Person();
        personUpdates.setUsername(editPersonDto.getUsername());
        personUpdates.setEmail(editPersonDto.getEmail());
        personUpdates.setGivenName(editPersonDto.getGivenName());
        personUpdates.setFamilyName(editPersonDto.getFamilyName());

        if (editPersonDto.getPassword() != null && !editPersonDto.getPassword().isEmpty()) {
            personUpdates.setPassword(editPersonDto.getPassword());
        }

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
        Person person = service.findById(id).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("person", person);

        List<Subscription> subscriptions = person.getSubscriptions()
                .stream()
                .sorted(Comparator.comparing(Subscription::getSince).reversed())
                .toList();
        model.addAttribute("subscriptions", subscriptions);
        return "people/listSubscriptionsForPerson";
    }
}
