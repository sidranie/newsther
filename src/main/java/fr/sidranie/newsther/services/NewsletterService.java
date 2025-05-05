package fr.sidranie.newsther.services;

import java.security.Principal;

import fr.sidranie.newsther.entities.Newsletter;

public interface NewsletterService {
    void createNewsletter(Newsletter newsletter, Principal principal);

    void deleteNewsletter(Long id);

    void deleteByCreatorId(Long creatorId);

    Newsletter editNewsletter(Newsletter newsletterTarget, Newsletter newsletterUpdates);
}
