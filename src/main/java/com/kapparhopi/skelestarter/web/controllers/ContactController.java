package com.kapparhopi.skelestarter.web.controllers;

import com.kapparhopi.skelestarter.backend.service.EmailService;
import com.kapparhopi.skelestarter.web.domain.frontend.FeedbackPojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class ContactController {

    /** The key which identifies the feedback payload in the Model. */
    public static final String FEEDBACK_MODEL_KEY = "feedback";

    /** The Contact Us view name. */
    private static final String CONTACT_US_VIEW_NAME = "contact/contact";

    @Autowired
    private EmailService emailService;

    @GetMapping("/contact")
    public String contactGet(ModelMap model) {
        FeedbackPojo feedback = new FeedbackPojo();
        model.addAttribute(ContactController.FEEDBACK_MODEL_KEY, feedback);
        return ContactController.CONTACT_US_VIEW_NAME;
    }

    @PostMapping("/contact")
    public String contactPost(@ModelAttribute(FEEDBACK_MODEL_KEY) FeedbackPojo feedback) {
        log.debug("Feedback POJO content: {}", feedback);
        emailService.sendFeedbackEmail(feedback);
        return ContactController.CONTACT_US_VIEW_NAME;
    }
}
