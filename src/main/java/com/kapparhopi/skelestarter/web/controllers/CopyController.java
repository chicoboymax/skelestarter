package com.kapparhopi.skelestarter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CopyController {

    private static final String ABOUT_VIEW_NAME = "copy/about";

    @GetMapping("/about")
    public String about() {
        return CopyController.ABOUT_VIEW_NAME;
    }
}
