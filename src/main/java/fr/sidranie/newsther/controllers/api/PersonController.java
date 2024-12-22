package fr.sidranie.newsther.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

import fr.sidranie.newsther.dtos.person.CreatePersonDto;
import fr.sidranie.newsther.dtos.person.FullPersonDto;
import fr.sidranie.newsther.dtos.person.ShortPersonDto;
import fr.sidranie.newsther.entities.Person;
import fr.sidranie.newsther.mappers.PersonMapper;
import fr.sidranie.newsther.services.PersonService;

@RestController
@RequestMapping("/api/people")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Set<ShortPersonDto>> findAll() {
        return ResponseEntity.ok(service.findAll()
                .stream().map(PersonMapper::personToShortPersonDto)
                .collect(Collectors.toSet()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullPersonDto> findById(@PathVariable("id") Long id) {
        return service.findById(id)
                .map(PersonMapper::personToFullPersonDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FullPersonDto> registerPerson(@RequestBody CreatePersonDto createPersonDto) {
        Person person = PersonMapper.createPersonDtoToPerson(createPersonDto);
        service.registerPerson(person);
        return ResponseEntity.created(URI.create("/people/" + person.getId()))
                .body(PersonMapper.personToFullPersonDto(person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePerson(@PathVariable("id") Long id) {
        // TODO Block deletion if he owns at least one channel
        // TODO Remove linked subscriptions
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
