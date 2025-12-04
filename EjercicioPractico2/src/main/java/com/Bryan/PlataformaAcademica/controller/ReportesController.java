package com.Bryan.PlataformaAcademica.controller;

import com.Bryan.PlataformaAcademica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/reportes")
@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
public class ReportesController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String reportes(Model model) {
        try {
            long totalUsuarios = usuarioService.contarTotalUsuarios();
            long usuariosActivos = usuarioService.contarUsuariosActivos();
            long usuariosInactivos = usuarioService.contarUsuariosInactivos();
            
            // Para estadísticas por rol - implementación simple
            var estadisticasPorRol = usuarioService.obtenerEstadisticasPorRol();
            
            // Para usuarios recientes
            var usuariosRecientes = usuarioService.obtenerUsuariosRecientes(7);
            
            model.addAttribute("totalUsuarios", totalUsuarios);
            model.addAttribute("usuariosActivos", usuariosActivos);
            model.addAttribute("usuariosInactivos", usuariosInactivos);
            model.addAttribute("estadisticasPorRol", estadisticasPorRol);
            model.addAttribute("usuariosRecientes", usuariosRecientes);
            model.addAttribute("pageTitle", "Reportes del Sistema");
            
            return "reportes";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar reportes: " + e.getMessage());
            return "reportes";
        }
    }
}