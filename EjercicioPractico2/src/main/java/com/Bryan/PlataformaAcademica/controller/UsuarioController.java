package com.Bryan.PlataformaAcademica.controller;

import com.Bryan.PlataformaAcademica.domain.Usuario;
import com.Bryan.PlataformaAcademica.service.RolService;
import com.Bryan.PlataformaAcademica.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("pageTitle", "Gestión de Usuarios");
        return "usuarios/lista";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.listarRoles());
        model.addAttribute("pageTitle", "Crear Nuevo Usuario");
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                               BindingResult result,
                               @RequestParam("confirmarPassword") String confirmarPassword,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("roles", rolService.listarRoles());
            return "usuarios/form";
        }
        
        // Validar contraseña
        if (usuario.getId() == null && (usuario.getPassword() == null || usuario.getPassword().isEmpty())) {
            result.rejectValue("password", "error.usuario", "La contraseña es obligatoria");
            model.addAttribute("roles", rolService.listarRoles());
            return "usuarios/form";
        }
        
        // Validar confirmación de contraseña para nuevos usuarios
        if (usuario.getId() == null && !usuario.getPassword().equals(confirmarPassword)) {
            result.rejectValue("password", "error.usuario", "Las contraseñas no coinciden");
            model.addAttribute("roles", rolService.listarRoles());
            return "usuarios/form";
        }
        
        // Validar email único
        if (usuario.getId() == null && usuarioService.existeEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.usuario", "Ya existe un usuario con este email");
            model.addAttribute("roles", rolService.listarRoles());
            return "usuarios/form";
        }
        
        // Validar email único al editar
        if (usuario.getId() != null && usuarioService.existeEmailYNoEsUsuarioActual(usuario.getEmail(), usuario.getId())) {
            result.rejectValue("email", "error.usuario", "Ya existe un usuario con este email");
            model.addAttribute("roles", rolService.listarRoles());
            return "usuarios/form";
        }
        
        // Manejar la contraseña
        if (usuario.getId() == null) {
            // Nuevo usuario: encriptar contraseña
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else {
            // Editar usuario: mantener contraseña existente si no se cambia
            Usuario usuarioExistente = usuarioService.obtenerPorId(usuario.getId());
            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                usuario.setPassword(usuarioExistente.getPassword());
            } else {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }
        
        usuarioService.guardar(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario guardado exitosamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return "redirect:/usuarios";
        }
        
        // No mostrar la contraseña al editar
        usuario.setPassword("");
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.listarRoles());
        model.addAttribute("pageTitle", "Editar Usuario");
        return "usuarios/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        return "redirect:/usuarios";
    }
    
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return "redirect:/usuarios";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("pageTitle", "Detalle del Usuario");
        return "usuarios/detalle";
    }
    
    @GetMapping("/activar/{id}")
    public String activarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario != null) {
            usuario.setActivo(true);
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario activado exitosamente");
        }
        return "redirect:/usuarios";
    }
    
    @GetMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario != null) {
            usuario.setActivo(false);
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario desactivado exitosamente");
        }
        return "redirect:/usuarios";
    }
}