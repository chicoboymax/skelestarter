package com.kapparhopi.skelestarter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author mdrouin
 * @since 2018-03-28
 */

@Controller
public class MainController {

    @RequestMapping("/")
    public String getIndex() {
        return "index";
    }
}
