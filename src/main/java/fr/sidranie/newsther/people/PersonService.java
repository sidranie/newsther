package fr.sidranie.newsther.people;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import fr.sidranie.newsther.newsletters.NewsletterService;
import fr.sidranie.newsther.subscriptions.Subscriptions;

@Service
public class PersonService implements UserDetailsService, UserDetailsPasswordService {

    private final People people;
    private final Subscriptions subscriptions;
    private final NewsletterService newsletterService;
    private final PasswordEncoder passwordEncoder;

    public PersonService(People people,
                         Subscriptions subscriptions,
                         NewsletterService newsletterService,
                         PasswordEncoder passwordEncoder) {
        this.people = people;
        this.subscriptions = subscriptions;
        this.newsletterService = newsletterService;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerPerson(Person person) {
        person.setId(null);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        people.save(person);
    }

    public Person updatePerson(Person person, Person personUpdates) {
        person.setUsername(personUpdates.getUsername());
        person.setEmail(personUpdates.getEmail());
        if (personUpdates.getPassword() != null && !personUpdates.getPassword().isEmpty()) {
            person.setPassword(passwordEncoder.encode(personUpdates.getPassword()));
        }
        person.setGivenName(personUpdates.getGivenName());
        person.setFamilyName(personUpdates.getFamilyName());
        people.save(person);
        return person;
    }

    public void deleteById(Long id) {
        this.subscriptions.deleteByPersonId(id);
        this.newsletterService.deleteByCreatorId(id);
        people.deleteById(id);
    }

    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Person person = people.findByUsernameOrEmail(user.getUsername(), user.getUsername())
                .orElseThrow(IllegalArgumentException::new);
        person.setPassword(passwordEncoder.encode(newPassword));
        people.save(person);
        return person;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return people.findByUsernameOrEmail(username, username).orElse(null);
    }
}
