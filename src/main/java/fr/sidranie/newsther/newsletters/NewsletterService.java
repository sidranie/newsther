package fr.sidranie.newsther.newsletters;

import java.security.Principal;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import fr.sidranie.newsther.news.Newses;
import fr.sidranie.newsther.people.People;
import fr.sidranie.newsther.subscriptions.Subscriptions;

@Service
public class NewsletterService {

    private final Newsletters newsletters;
    private final People people;
    private final Newses newses;
    private final Subscriptions subscriptions;

    public NewsletterService(Newsletters newsletters,
                             People people,
                             Newses newses,
                             Subscriptions subscriptions) {
        this.newsletters = newsletters;
        this.people = people;
        this.newses = newses;
        this.subscriptions = subscriptions;
    }

    public void createNewsletter(Newsletter newsletter, Principal principal) {
        newsletter.setId(null);
        newsletter.setCreator(people.findByUsernameOrEmail(principal.getName(), principal.getName())
                .orElseThrow(IllegalAccessError::new));
        newsletter.setSlug(titleToSlug(newsletter.getTitle()));
        newsletters.save(newsletter);
    }

    public void deleteNewsletter(Long id) {
        newses.deleteByNewsletterId(id);
        subscriptions.deleteByNewsletterId(id);
        newsletters.deleteById(id);
    }

    public void deleteByCreatorId(Long creatorId) {
        newsletters.findByCreatorId(creatorId).stream()
            .map(Newsletter::getId)
            .forEach(this::deleteNewsletter);
    }

    public Newsletter editNewsletter(Newsletter newsletterTarget, Newsletter newsletterUpdates) {
        newsletterTarget.setTitle(newsletterUpdates.getTitle());
        newsletterTarget.setSlug(titleToSlug(newsletterUpdates.getTitle()));
        newsletters.save(newsletterTarget);
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
