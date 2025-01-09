package fr.sidranie.newsther.controllers.renderers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralRenderer {

    /**
     * Endpoint to render the main page for anonymous users
     */
    @GetMapping
    public String renderIndex() {
        return "index";
    }

    /**
     * Endpoint to render the main page for logged users
     */
    @GetMapping("/home")
    public String renderHome() {
        return "home";
    }

    @GetMapping("/login")
    public String renderLogin() {
        return "login";
    }
}
