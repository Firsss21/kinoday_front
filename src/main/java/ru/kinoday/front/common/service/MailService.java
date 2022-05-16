package ru.kinoday.front.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    String username;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        new Thread(() -> emailSender.send(message)).run();
    }
}
