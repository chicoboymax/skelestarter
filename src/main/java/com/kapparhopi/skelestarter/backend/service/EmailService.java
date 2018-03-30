package com.kapparhopi.skelestarter.backend.service;

import com.kapparhopi.skelestarter.web.domain.frontend.FeedbackPojo;

import org.springframework.mail.SimpleMailMessage;

/**
 * @author mdrouin
 * @since 2018-03-30
 */
public interface EmailService {

    /**
     * Sends an email with the content in the Feedback Pojo.
     *
     * @param feedbackPojo The feedback Pojo
     */
    public void sendFeedbackEmail(FeedbackPojo feedbackPojo);

    /**
     * Sends an email with the content of the Simple Mail Message object.
     *
     * @param message The object containing the email content
     */
    public void sendGenericEmailMessage(SimpleMailMessage message);
}
