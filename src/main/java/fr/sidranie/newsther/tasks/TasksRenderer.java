package fr.sidranie.newsther.tasks;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import fr.sidranie.newsther.email.EmailService;
import jakarta.mail.MessagingException;

@Controller
@RequestMapping("/tasks")
public class TasksRenderer {

    EmailService emailService;

    public TasksRenderer(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-news")
    public String performNewsSending() throws MessagingException {
        emailService.sendEmailToEveryone();
        return "redirect:/";
    }
}
