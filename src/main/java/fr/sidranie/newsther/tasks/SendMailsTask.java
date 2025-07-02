package fr.sidranie.newsther.tasks;

import java.time.Instant;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import fr.sidranie.newsther.news.News;
import fr.sidranie.newsther.newsletters.Newsletter;
import fr.sidranie.newsther.newsletters.Newsletters;
import fr.sidranie.newsther.people.People;
import fr.sidranie.newsther.people.Person;
import fr.sidranie.newsther.subscriptions.Subscription;
import fr.sidranie.newsther.util.CountedObject;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class SendMailsTask {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final People people;
    private final Newsletters newsletters;

    @Value("${spring.mail.username}")
    private String fromMail;
    @Value("${newsther.mailing.retry.limit}")
    private int retryLimit;
    @Value("${newsther.mailing.mail-subject}")
    private String mailSubject;

    public SendMailsTask(JavaMailSender mailSender, TemplateEngine templateEngine, People people, Newsletters newsletters) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.people = people;
        this.newsletters = newsletters;
    }

    @Transactional
    @Scheduled(cron = "${newsther.mailing.cron-trigger}")
    public void sendMailsTask() throws MessagingException {
        Map<Long, News> sendableNewses = this.findNewsesToSend();

        Queue<CountedObject<Person>> peopleQueue = this.people.findAll().stream()
            .filter(person -> !person.getSubscriptions().isEmpty())
            .sorted(Comparator.comparing(Person::getId))
            .map(CountedObject::new)
            .collect(Collectors.toCollection(LinkedList::new));

        for (CountedObject<Person> countedPerson = peopleQueue.poll();
             countedPerson != null;
             countedPerson = peopleQueue.poll()) {

            List<Long> subscribedNewsletters = countedPerson.getValue()
                    .getSubscriptions()
                    .stream()
                    .map(Subscription::getNewsletter)
                    .map(Newsletter::getId)
                    .toList();

            List<News> newsInMail = sendableNewses.entrySet().stream()
                    .filter(entry -> subscribedNewsletters.contains(entry.getKey()))
                    .map(Entry::getValue)
                    .toList();

            MimeMessage mail = buildMail(countedPerson.getValue(), newsInMail);
            try {
                this.mailSender.send(mail);
            } catch (MailException e) {
                if (countedPerson.getCounter() < retryLimit) {
                    countedPerson.incrementCounter();
                    peopleQueue.add(countedPerson);
                }
            }
        }

        Instant now = Instant.now();
        sendableNewses.values().forEach(news -> news.setSendDate(now));
    }

    private Map<Long, News> findNewsesToSend() {
        return this.newsletters.findAll()
            .stream()
            .filter(newsletter -> !newsletter.getNews().isEmpty())
            .map(newsletter -> { // Get newses to send. Identified by their newsletter id
                List<News> notSentNewses = newsletter.getNews()
                    .stream()
                    .filter(news -> Objects.isNull(news.getSendDate()))
                    .sorted(Comparator.comparing(News::getCreationDate))
                    .toList();
                return notSentNewses.isEmpty() ? null :
                        new AbstractMap.SimpleEntry<>(newsletter.getId(), notSentNewses.getFirst());
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private MimeMessage buildMail(Person person, List<News> newsList) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setTo(person.getEmail());
        mimeMessageHelper.setSubject(this.mailSubject);

        Context context = new Context();
        context.setVariable("newsList", newsList);
        String processedString = templateEngine.process("mail_template", context);

        mimeMessageHelper.setText(processedString, true);

        return mimeMessage;
    }
}
