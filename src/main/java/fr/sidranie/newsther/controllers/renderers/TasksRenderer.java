package fr.sidranie.newsther.controllers.renderers;

import fr.sidranie.newsther.services.EmailService;
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
    public void sendNews() {
        emailService.sendEmailToEveryone();
    }
}
