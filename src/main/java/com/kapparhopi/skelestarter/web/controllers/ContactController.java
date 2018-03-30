package com.kapparhopi.skelestarter.web.controllers;

import com.kapparhopi.skelestarter.web.domain.frontend.Feedback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class ContactController {

    /** The key which identifies the feedback payload in the Model. */
    public static final String FEEDBACK_MODEL_KEY = "feedback";

    /** The Contact Us view name. */
    private static final String CONTACT_US_VIEW_NAME = "contact/contact";

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactGet(ModelMap model) {
        Feedback feedback = new Feedback();
        model.addAttribute(ContactController.FEEDBACK_MODEL_KEY, feedback);
        return ContactController.CONTACT_US_VIEW_NAME;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String contactPost(@ModelAttribute(FEEDBACK_MODEL_KEY) Feedback feedback) {
        log.debug("Feedback POJO content: {}", feedback);
        emailService.sendFeedbackEmail(feedback);
        return ContactController.CONTACT_US_VIEW_NAME;
    }
}
