package com.pshiblo.videohosting.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Максим Пшибло
 */
@Controller("asdasdasd/asdsad/a")
public class HomeController {


    @GetMapping
    public String getHome() {
        return "index";
    }
}
