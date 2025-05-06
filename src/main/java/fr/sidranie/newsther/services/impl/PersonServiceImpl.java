package fr.sidranie.newsther.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import fr.sidranie.newsther.entities.Person;
import fr.sidranie.newsther.repositories.PersonRepository;
import fr.sidranie.newsther.repositories.SubscriptionRepository;
import fr.sidranie.newsther.services.NewsletterService;
import fr.sidranie.newsther.services.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;
    private final SubscriptionRepository subscriptionRepository;
    private final NewsletterService newsletterService;
    private final PasswordEncoder passwordEncoder;

    public PersonServiceImpl(PersonRepository repository,
            SubscriptionRepository subscriptionRepository,
            NewsletterService newsletterService,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.subscriptionRepository = subscriptionRepository;
        this.newsletterService = newsletterService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Set<Person> findAll() {
        Set<Person> people = new HashSet<>();
        repository.findAll().forEach(people::add);
        return people;
    }

    @Override
    public Optional<Person> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Person> findByUsernameOrEmail(String identifier) {
        return repository.findByUsernameOrEmail(identifier, identifier);
    }

    @Override
    public void registerPerson(Person person) {
        person.setId(null);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        repository.save(person);
    }

    @Override
    public Person updatePerson(Person person, Person personUpdates) {
        person.setUsername(personUpdates.getUsername());
        person.setEmail(personUpdates.getEmail());
        if (personUpdates.getPassword() != null && !personUpdates.getPassword().isEmpty()) {
            person.setPassword(passwordEncoder.encode(personUpdates.getPassword()));
        }
        person.setGivenName(personUpdates.getGivenName());
        person.setFamilyName(personUpdates.getFamilyName());
        repository.save(person);
        return person;
    }

    @Override
    public void deleteById(Long id) {
        this.subscriptionRepository.deleteByPersonId(id);
        this.newsletterService.deleteByCreatorId(id);
        repository.deleteById(id);
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Person person = findByUsernameOrEmail(user.getUsername())
                .orElseThrow(IllegalArgumentException::new);
        person.setPassword(passwordEncoder.encode(newPassword));
        repository.save(person);
        return person;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsernameOrEmail(username).orElse(null);
    }
}
