package fr.sidranie.newsther.tasks;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.stream.Collectors;

import fr.sidranie.newsther.email.EmailService;
import fr.sidranie.newsther.news.NewsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import fr.sidranie.newsther.news.News;
import fr.sidranie.newsther.people.People;
import fr.sidranie.newsther.people.Person;
import fr.sidranie.newsther.util.CountedObject;
import jakarta.mail.MessagingException;

@Component
public class SendMailsTask {

    private final People people;
    private final NewsService newsService;
    private final EmailService emailService;

    @Value("${newsther.mailing.retry.limit}")
    private int retryLimit;
    @Value("${newsther.mailing.mail-subject}")
    private String mailSubject;

    public SendMailsTask(People people,
                         NewsService newsService,
                         EmailService emailService) {
        this.people = people;
        this.newsService = newsService;
        this.emailService = emailService;
    }

    @Transactional
    @Scheduled(cron = "${newsther.mailing.cron-trigger}")
    public void sendMailsTask() throws MessagingException {
        Map<Long, News> sendableNewses = newsService.findNewsesToSend();

        Queue<CountedObject<Person>> peopleQueue = this.people.findAll().stream()
            .filter(Person::hasSubscriptions)
            .sorted(Comparator.comparing(Person::getId))
            .map(CountedObject::new)
            .collect(Collectors.toCollection(LinkedList::new));

        for (CountedObject<Person> countedPerson = peopleQueue.poll();
             countedPerson != null;
             countedPerson = peopleQueue.poll()) {

            Person person = countedPerson.getValue();
            List<Long> subscribedNewsletters = person.getSubscriptions().stream()
                    .map(subscription -> subscription.getNewsletter().getId())
                    .toList();

            List<News> newsInMail = sendableNewses.entrySet().stream()
                    .filter(entry -> subscribedNewsletters.contains(entry.getKey()))
                    .map(Entry::getValue)
                    .toList();

            try {
                this.sendMail(person, newsInMail);
            } catch (MailException e) {
                if (countedPerson.getCounter() < retryLimit) {
                    countedPerson.incrementCounter();
                    peopleQueue.add(countedPerson);
                }
            }
        }

        Instant now = Instant.now(); // Fix send datetime for all newses
        sendableNewses.values().forEach(news -> news.setSendDate(now));
    }

    private void sendMail(Person person, List<News> newsList) throws MessagingException {
        Context context = new Context();
        context.setVariable("newsList", newsList);

        emailService.sendMail(person.getEmail(), this.mailSubject, "mail_template", context);
    }
}
