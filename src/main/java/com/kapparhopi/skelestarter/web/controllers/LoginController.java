package com.kapparhopi.skelestarter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author mdrouin
 * @since 2018-03-30
 */

@Controller
public class LoginController {

    public static final String LOGIN_VIEW_NAME = "user/login";

    @GetMapping("/login")
    public String login() {
        return LOGIN_VIEW_NAME;
    }
}
