package com.slim.authentification.services;

import com.slim.authentification.repositories.EmailSenderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author slimane
 * @Project emailSpring
 */

@Service
public class EmailSendService implements EmailSenderRepository {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailSendService.class);
    private final JavaMailSender javaMailSender;

    public EmailSendService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String to, String email){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("testmailcoolspring@gmail.com");
            javaMailSender.send(mimeMessage);
        } catch (
                MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

}
