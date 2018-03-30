package com.kapparhopi.skelestarter.backend.service;

import org.springframework.mail.SimpleMailMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mdrouin
 * @since 2018-03-30
 */

@Slf4j
public class MockEmailService extends AbstractEmailService {

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage message) {
        log.debug("Simulating an email service...");
        log.info(message.toString());
        log.debug("Email sent.");
    }
}
