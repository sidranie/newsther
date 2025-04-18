package fr.sidranie.newsther.services.impl;

import fr.sidranie.newsther.entities.News;
import fr.sidranie.newsther.entities.Newsletter;
import fr.sidranie.newsther.entities.Person;
import fr.sidranie.newsther.entities.Subscription;
import fr.sidranie.newsther.services.EmailService;
import fr.sidranie.newsther.services.PersonService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final PersonService personService;

    @Value("${spring.mail.username}")
    private String fromMail;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, PersonService personService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.personService = personService;
    }

    public void sendEmail(String to, String subject, String template, Context context) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Process the template with the given context
        String htmlContent = templateEngine.process(template, context);

        // Set email properties
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // Set true for HTML content

        // Send the email
        mailSender.send(mimeMessage);
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

            try {
                this.sendNewsTo(person, newsList);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });

        Instant now = Instant.now();
        sentNews.forEach(news -> news.setSendDate(now));
    }

    private void sendNewsTo(Person person, List<News> newsList) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setTo(person.getEmail());
        mimeMessageHelper.setSubject("Newsther weekly");

        Context context = new Context();
        context.setVariable("newsList", newsList);
        String processedString = templateEngine.process("mail_template", context);

        mimeMessageHelper.setText(processedString, true);

        mailSender.send(mimeMessage);
    }

    private List<News> getNewsToSendFor(Person person) {
        return person.getSubscriptions()
                .stream()
                .map(Subscription::getNewsletter)
                .map(Newsletter::getNews)
                .flatMap(Set::stream)
                .filter(news -> Objects.isNull(news.getSendDate()))
                .toList();
    }
}
