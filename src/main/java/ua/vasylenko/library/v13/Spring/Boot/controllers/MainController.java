package ua.vasylenko.library.v13.Spring.Boot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String mainPage() {
        return "mainPage";
    }
}
