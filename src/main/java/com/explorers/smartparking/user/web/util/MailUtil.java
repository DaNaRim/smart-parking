package com.explorers.smartparking.user.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

@Component
public class MailUtil {

    private final Environment environment;
    private final MessageSource messages;
    private final JavaMailSender mailSender;

    @Autowired
    public MailUtil(Environment environment, MessageSource messages, JavaMailSender mailSender) {
        this.environment = environment;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    public void sendVerificationTokenEmail(String contextPath, //TODO locale in links
                                           Locale locale,
                                           String token,
                                           String userEmail) {

        String url = String.format("%s/registrationConfirm?token=%s", contextPath, token);
        String message = messages.getMessage("message.email.btn.enableAccount", null, locale);
        mailSender.send(constructEmail("Enable Account",
                message + "\r\n" + url, userEmail));
    }

    public void sendResetPasswordTokenEmail(String contextPath,
                                            Locale locale,
                                            String token,
                                            String userEmail) {

        String url = String.format("%s/updateForgottenPassword?token=%s", contextPath, token);
        String message = messages.getMessage("message.email.btn.resetPassword", null, locale);
        mailSender.send(constructEmail("Reset Password", message + "\r\n" + url, userEmail));
    }

    private SimpleMailMessage constructEmail(String subject, String body, String userEmail) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(userEmail);
        email.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
        return email;
    }
}
