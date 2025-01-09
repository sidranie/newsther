package fr.sidranie.newsther.controllers.renderers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralRenderer {

    @GetMapping
    public String renderIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String renderLogin() {
        return "login";
    }
}
