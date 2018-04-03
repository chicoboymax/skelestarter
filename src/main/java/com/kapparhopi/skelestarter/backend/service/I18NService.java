package com.kapparhopi.skelestarter.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * @author mdrouin
 * @since 2018-03-29
 */
@Service
@Slf4j
public class I18NService {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String messageId) {
        log.info("Returning i18n text for messageId {}", messageId);
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(messageId, locale);
    }

    public String getMessage(String messageId, Locale locale) {
        return messageSource.getMessage(messageId, null, locale);
    }

}
