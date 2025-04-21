package fr.sidranie.newsther.services;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmailToEveryone() throws MessagingException;
}
