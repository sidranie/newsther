package fr.sidranie.newsther.people;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import fr.sidranie.newsther.newsletters.NewsletterService;
import fr.sidranie.newsther.subscriptions.SubscriptionRepository;

@Service
public class PersonService implements UserDetailsService, UserDetailsPasswordService {

    private final PersonRepository repository;
    private final SubscriptionRepository subscriptionRepository;
    private final NewsletterService newsletterService;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository repository,
                         SubscriptionRepository subscriptionRepository,
                         NewsletterService newsletterService,
                         PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.subscriptionRepository = subscriptionRepository;
        this.newsletterService = newsletterService;
        this.passwordEncoder = passwordEncoder;
    }

    public Set<Person> findAll() {
        Set<Person> people = new HashSet<>();
        repository.findAll().forEach(people::add);
        return people;
    }

    public Optional<Person> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Person> findByUsernameOrEmail(String identifier) {
        return repository.findByUsernameOrEmail(identifier, identifier);
    }

    public void registerPerson(Person person) {
        person.setId(null);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        repository.save(person);
    }

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

    public void deleteById(Long id) {
        this.subscriptionRepository.deleteByPersonId(id);
        this.newsletterService.deleteByCreatorId(id);
        repository.deleteById(id);
    }

    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Person person = findByUsernameOrEmail(user.getUsername())
                .orElseThrow(IllegalArgumentException::new);
        person.setPassword(passwordEncoder.encode(newPassword));
        repository.save(person);
        return person;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsernameOrEmail(username).orElse(null);
    }
}
