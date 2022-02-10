package com.example.chatapi.springbootfirebase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public void sendMailWithAttachment(String toEmail, String password,
                                       String body,
                                       String subject) throws MessagingException {


        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.from", "noreply@chat-api-132b7.firebaseapp.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.password", "password");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");

        javaMailSender.setPassword(password);

        javaMailSender.setJavaMailProperties(props);

        SimpleMailMessage mimeMessage=new SimpleMailMessage();
        mimeMessage.setFrom("noreply@chat-api-132b7.firebaseapp.com");

        mimeMessage.setTo(toEmail);
        mimeMessage.setText(body);
        mimeMessage.setSubject(subject);

        javaMailSender.send(mimeMessage);
        System.out.println("Mail with attachment sent successfully..");
    }
}