package com.kapparhopi.skelestarter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author mdrouin
 * @since 2018-03-28
 */

@Controller
public class IndexController {

    private static final String INDEX_VIEW_NAME = "index";

    @GetMapping("/")
    public String home() {
        return INDEX_VIEW_NAME;
    }
}
