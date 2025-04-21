package fr.sidranie.newsther.controllers.renderers;

import fr.sidranie.newsther.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
public class TasksRenderer {

    EmailService emailService;

    public TasksRenderer(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-news")
    public void sendNews() throws MessagingException {
        emailService.sendEmailToEveryone();
    }
}
