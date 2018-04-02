package com.kapparhopi.skelestarter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author mdrouin
 * @since 2018-03-30
 */

@Controller
public class PayloadController {

    public static final String PAYLOAD_VIEW_NAME = "payload/payload";

    @GetMapping("/payload")
    public String payload() {
        return PAYLOAD_VIEW_NAME;
    }
}
