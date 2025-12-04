package com.Bryan.PlataformaAcademica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       @RequestParam(value = "registro", required = false) String registro,
                       Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Correo electrónico o contraseña incorrectos.");
        }
        
        if (logout != null) {
            model.addAttribute("mensaje", "Has cerrado sesión exitosamente.");
        }
        
        if (registro != null) {
            model.addAttribute("mensaje", "Usuario registrado exitosamente. Por favor, inicia sesión.");
        }
        
        model.addAttribute("pageTitle", "Iniciar Sesión");
        return "login";
    }
}