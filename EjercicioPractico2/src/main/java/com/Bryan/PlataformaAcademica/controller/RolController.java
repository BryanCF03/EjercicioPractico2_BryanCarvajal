package com.Bryan.PlataformaAcademica.controller;

import com.Bryan.PlataformaAcademica.domain.Rol;
import com.Bryan.PlataformaAcademica.service.RolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public String listarRoles(Model model) {
        model.addAttribute("roles", rolService.listarRoles());
        model.addAttribute("pageTitle", "Gestión de Roles");
        return "roles/lista";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("rol", new Rol());
        model.addAttribute("pageTitle", "Crear Nuevo Rol");
        return "roles/form";
    }

    @PostMapping("/guardar")
    public String guardarRol(@Valid @ModelAttribute("rol") Rol rol, 
                           BindingResult result, 
                           RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "roles/form";
        }
        
        // Validar si ya existe un rol con ese nombre
        if (rol.getId() == null && rolService.existePorNombre(rol.getNombre())) {
            result.rejectValue("nombre", "error.rol", "Ya existe un rol con este nombre");
            return "roles/form";
        }
        
        rolService.guardar(rol);
        redirectAttributes.addFlashAttribute("mensaje", "Rol guardado exitosamente");
        return "redirect:/roles";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Rol rol = rolService.obtenerPorId(id);
        if (rol == null) {
            return "redirect:/roles";
        }
        
        model.addAttribute("rol", rol);
        model.addAttribute("pageTitle", "Editar Rol");
        return "roles/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRol(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rolService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Rol eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el rol porque tiene usuarios asignados");
        }
        return "redirect:/roles";
    }
    
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Rol rol = rolService.obtenerPorId(id);
        if (rol == null) {
            return "redirect:/roles";
        }
        
        model.addAttribute("rol", rol);
        model.addAttribute("pageTitle", "Detalle del Rol");
        return "roles/detalle";
    }
}