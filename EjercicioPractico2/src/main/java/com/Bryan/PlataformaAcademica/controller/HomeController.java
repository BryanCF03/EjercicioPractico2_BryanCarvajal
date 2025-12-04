package com.Bryan.PlataformaAcademica.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String role = auth.getAuthorities().toString();
        
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("pageTitle", "Inicio - Plataforma Académica");
        
        return "home";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }
}