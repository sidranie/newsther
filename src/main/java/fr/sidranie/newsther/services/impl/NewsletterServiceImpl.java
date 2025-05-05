package fr.sidranie.newsther.services.impl;

import java.security.Principal;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import fr.sidranie.newsther.entities.Newsletter;
import fr.sidranie.newsther.repositories.NewsRepository;
import fr.sidranie.newsther.repositories.NewsletterRepository;
import fr.sidranie.newsther.repositories.PersonRepository;
import fr.sidranie.newsther.repositories.SubscriptionRepository;
import fr.sidranie.newsther.services.NewsletterService;

@Service
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository repository;
    private final PersonRepository personRepository;
    private final NewsRepository newsRepository;
    private final SubscriptionRepository subscriptionRepository;

    public NewsletterServiceImpl(NewsletterRepository repository,
                                    PersonRepository personRepository,
                                    NewsRepository newsRepository,
                                    SubscriptionRepository subscriptionRepository) {
        this.repository = repository;
        this.personRepository = personRepository;
        this.newsRepository = newsRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void createNewsletter(Newsletter newsletter, Principal principal) {
        newsletter.setId(null);
        newsletter.setCreator(personRepository.findByUsernameOrEmail(principal.getName(), principal.getName())
                .orElseThrow(IllegalAccessError::new));
        newsletter.setSlug(titleToSlug(newsletter.getTitle()));
        repository.save(newsletter);
    }

    @Override
    public void deleteNewsletter(Long id) {
        newsRepository.deleteByNewsletterId(id);
        subscriptionRepository.deleteByNewsletterId(id);
        repository.deleteById(id);
    }

    @Override
    public void deleteByCreatorId(Long creatorId) {
        repository.findByCreatorId(creatorId).stream()
            .map(Newsletter::getId)
            .forEach(this::deleteNewsletter);
    }

    @Override
    public Newsletter editNewsletter(Newsletter newsletterTarget, Newsletter newsletterUpdates) {
        newsletterTarget.setTitle(newsletterUpdates.getTitle());
        newsletterTarget.setSlug(titleToSlug(newsletterUpdates.getTitle()));
        repository.save(newsletterTarget);
        return newsletterTarget;
    }

    private String titleToSlug(String title) {
        final Pattern NONLATIN = Pattern.compile("[^\\w-]");
        final Pattern WHITESPACE = Pattern.compile("[\\s]");
        String nowhitespace = WHITESPACE.matcher(title).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.FRANCE);
    }
}
