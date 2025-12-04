package com.Bryan.PlataformaAcademica.controller;

import com.Bryan.PlataformaAcademica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String role = auth.getAuthorities().toString();
        
        long totalUsuarios = usuarioService.contarTotalUsuarios();
        long usuariosActivos = usuarioService.contarUsuariosActivos();
        long usuariosInactivos = usuarioService.contarUsuariosInactivos();
        
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("usuariosInactivos", usuariosInactivos);
        model.addAttribute("pageTitle", "Dashboard - Plataforma Académica");
        
        return "home";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    
    @GetMapping("/acceso-denegado")
    public String accesoDenegado(Model model) {
        model.addAttribute("pageTitle", "Acceso Denegado");
        return "acceso-denegado";
    }
}