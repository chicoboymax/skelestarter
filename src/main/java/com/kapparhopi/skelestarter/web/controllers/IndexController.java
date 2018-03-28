package com.kapparhopi.skelestarter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.ws.RequestWrapper;

/**
 * @author mdrouin
 * @since 2018-03-28
 */

@Controller
public class IndexController {

    @RequestMapping("/")
    public String home(){
        return "index";
    }
}
