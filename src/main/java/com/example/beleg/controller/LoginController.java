package com.example.beleg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller fuer die Login-Seite der Anwendung.
 * Die eigentliche Authentifizierung wird von Spring Security uebernommen.
 */
@Controller
public class LoginController {

    /**
     * Zeigt die Login-Seite an.
     *
     * @return Name des Thymeleaf-Templates fuer die Login-Seite
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}