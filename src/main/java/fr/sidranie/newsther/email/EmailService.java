package fr.sidranie.newsther.email;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import fr.sidranie.newsther.news.News;
import fr.sidranie.newsther.news.Newses;
import fr.sidranie.newsther.newsletters.Newsletter;
import fr.sidranie.newsther.people.Person;
import fr.sidranie.newsther.people.PersonService;
import fr.sidranie.newsther.subscriptions.Subscription;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final PersonService personService;
    private final Newses newses;

    @Value("${spring.mail.username}")
    private String fromMail;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, PersonService personService, Newses newses) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.personService = personService;
        this.newses = newses;
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

    public void sendEmailToEveryone() throws MessagingException {
        List<News> sentNews = new ArrayList<>();

        for (Person person: personService.findAll()) {
            List<News> newsList = getNewsToSendFor(person);
            if (!newsList.isEmpty()) {
                System.out.println("News list to send for " + person.getUsername() + ": ");
                sendNewsTo(person, newsList);
                sentNews.addAll(newsList);
            }
        }

        Instant now = Instant.now();
        sentNews.forEach(news -> news.setSendDate(now));
        this.newses.saveAll(sentNews);
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
