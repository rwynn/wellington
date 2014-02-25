package org.github.rwynn.wellington.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(
            @RequestParam(value = "error", required = false) Boolean error,
            @RequestParam(value = "logout", required = false) Boolean logout,
            Model model) {
        if (error != null && error.booleanValue()) {
            model.addAttribute("loginError", true);
        }
        if (logout != null && logout.booleanValue()) {
            model.addAttribute("loggedOut", true);
        }
        return "login";
    }

}
