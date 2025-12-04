package com.Bryan.PlataformaAcademica.controller;

import com.Bryan.PlataformaAcademica.domain.Usuario;
import com.Bryan.PlataformaAcademica.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String verPerfil(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.findByEmail(email);
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        // No mostrar la contraseña
        usuario.setPassword("");
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("pageTitle", "Mi Perfil");
        return "perfil";
    }

    @PostMapping("/actualizar")
    public String actualizarPerfil(@Valid @ModelAttribute("usuario") Usuario usuario,
                                  BindingResult result,
                                  @RequestParam(value = "nuevaPassword", required = false) String nuevaPassword,
                                  @RequestParam(value = "confirmarPassword", required = false) String confirmarPassword,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Mi Perfil");
            return "perfil";
        }
        
        // Obtener el usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailActual = auth.getName();
        Usuario usuarioActual = usuarioService.findByEmail(emailActual);
        
        // Verificar que el usuario sea el mismo
        if (!usuarioActual.getId().equals(usuario.getId())) {
            return "redirect:/perfil";
        }
        
        // Manejar cambio de contraseña
        if (nuevaPassword != null && !nuevaPassword.isEmpty()) {
            if (!nuevaPassword.equals(confirmarPassword)) {
                model.addAttribute("errorPassword", "Las contraseñas no coinciden");
                model.addAttribute("pageTitle", "Mi Perfil");
                return "perfil";
            }
            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        } else {
            // Mantener la contraseña actual
            usuario.setPassword(usuarioActual.getPassword());
        }
        
        usuarioService.guardar(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado exitosamente");
        return "redirect:/perfil";
    }
}