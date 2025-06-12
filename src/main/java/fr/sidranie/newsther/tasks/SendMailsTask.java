package fr.sidranie.newsther.tasks;

import java.util.*;

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

    @Value("${spring.mail.username}")
    private String fromMail;
    @Value("${newsther.mailing.retry.limit}")
    private int retryLimit;
    @Value("${newsther.mailing.mail-subject}")
    private String mailSubject;

    public SendMailsTask(JavaMailSender mailSender, TemplateEngine templateEngine, People people) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.people = people;
    }

    @Transactional
    @Scheduled(cron = "${newsther.mailing.cron-trigger}")
    public void sendMailsTask() throws MessagingException {
        Queue<CountedObject<MimeMessage>> mailsQueue = this.buildMailTemplates();

        for (CountedObject<MimeMessage> countedMail = mailsQueue.poll();
             countedMail != null;
             countedMail = mailsQueue.poll()) {

            MimeMessage mail = countedMail.getValue();
            try {
                this.mailSender.send(mail);

            } catch (MailException e) {
                if (countedMail.getCounter() < retryLimit) {
                    countedMail.incrementCounter();
                    mailsQueue.add(countedMail);
                }
            }
        }
    }

    private Queue<CountedObject<MimeMessage>> buildMailTemplates() throws MessagingException {
        Queue<CountedObject<MimeMessage>> mailsQueue = new LinkedList<>();
        for (Person person: people.findAll()) {
            List<News> newsList = getNewsToSendFor(person);
            mailsQueue.add(new CountedObject<>(buildMail(person, newsList)));
        }
        return mailsQueue;
    }

    private List<News> getNewsToSendFor(Person person) {
        List<News> newsesToSend = new ArrayList<>();

        List<Newsletter> newsletters = person.getSubscriptions()
                .stream()
                .map(Subscription::getNewsletter)
                .toList();

        List<News> notSentNewses;
        for (Newsletter newsletter: newsletters) {
            notSentNewses = newsletter.getNews().stream()
                    .filter(news -> Objects.isNull(news.getSendDate()))
                    .sorted(Comparator.comparing(News::getCreationDate))
                    .toList();
            newsesToSend.add(notSentNewses.getFirst());
        }

        return newsesToSend.stream().sorted(Comparator.comparing(News::getCreationDate)).toList();
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
