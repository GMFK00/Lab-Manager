package com.labmanager.labmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Mapeia a página inicial
    @GetMapping("/")
    public String home() {
        return "index";  // Nome da página Thymeleaf (index.html)
    }

    // Mapeia a página home (para redirecionamento após login)
    @GetMapping("/home")
    public String userHome(Model model) {
        model.addAttribute("page", "home");  // Define a página ativa
        return "home";  // Nome da página Thymeleaf (home.html)
    }
}
