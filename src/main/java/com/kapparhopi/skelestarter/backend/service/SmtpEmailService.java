package com.kapparhopi.skelestarter.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mdrouin
 * @since 2018-03-30
 */

@Slf4j
public class SmtpEmailService extends AbstractEmailService {

    @Autowired
    private MailSender mailSender;

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage message) {
        log.debug("Sending email for: {}", message);
        mailSender.send(message);
        log.info("Email sent.");
    }
}
