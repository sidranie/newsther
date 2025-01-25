package fr.sidranie.newsther.services.impl;

import fr.sidranie.newsther.entities.News;
import fr.sidranie.newsther.entities.Newsletter;
import fr.sidranie.newsther.entities.Person;
import fr.sidranie.newsther.entities.Subscription;
import fr.sidranie.newsther.services.EmailService;
import fr.sidranie.newsther.services.PersonService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EmailServiceImpl implements EmailService {
    private PersonService personService;

    public EmailServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void sendEmailToEveryone() {
        List<News> sentNews = new ArrayList<>();

        personService.findAll().forEach(person -> {
            List<News> newsList = getNewsToSendFor(person);

            System.out.println("News list to send for " + person.getUsername() + ": ");

            StringBuilder builder = new StringBuilder()
                    .append("Newsther")
                    .append('\n');

            newsList.forEach(news -> {
                System.out.println("Add '" + news.getTitle() + "' to email");
                builder.append(news.toEmail()).append('\n');
                sentNews.add(news);
            });

            System.out.println("Send mail to " + person.getUsername() + ": '\n" + builder + "\n'");
        });

        Instant now = Instant.now();
        sentNews.forEach(news -> news.setSendDate(now));
    }

    private List<News> getNewsToSendFor(Person person) {
        return person.getSubscriptions()
                .stream()
                .map(Subscription::getNewsletter)
                .map(Newsletter::getNews)
                .flatMap(Set::stream)
                .filter(news -> news.getSendDate() == null)
                .toList();
    }
}
