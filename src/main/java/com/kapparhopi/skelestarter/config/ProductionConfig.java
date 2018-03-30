package com.kapparhopi.skelestarter.config;

import com.kapparhopi.skelestarter.backend.service.EmailService;
import com.kapparhopi.skelestarter.backend.service.SmtpEmailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * @author mdrouin
 * @since 2018-03-30
 */

@Configuration
@Profile("prod")
@PropertySource("application-prod.properties")
public class ProductionConfig {

    @Value("${stripe.prod.private.key}")
    private String stripeProdKey;

    @Bean
    public EmailService emailService() {
        return new SmtpEmailService();
    }

    @Bean
    public String stripeKey() {
        return stripeProdKey;
    }
}
