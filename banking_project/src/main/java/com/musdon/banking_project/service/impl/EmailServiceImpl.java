package com.musdon.banking_project.service.impl;

import com.musdon.banking_project.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(senderEmail);
                mailMessage.setTo(emailDetails.getRecipient());
                mailMessage.setSubject(emailDetails.getSubject());
                mailMessage.setText(emailDetails.getMessageBody());

                javaMailSender.send(mailMessage);
                System.out.println("Mail sent successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
