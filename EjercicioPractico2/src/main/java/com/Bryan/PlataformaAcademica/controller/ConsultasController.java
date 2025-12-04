package com.Bryan.PlataformaAcademica.controller;

import com.Bryan.PlataformaAcademica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/consultas")
@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
public class ConsultasController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String consultas(Model model) {
        try {
            var usuariosOrdenados = usuarioService.obtenerUsuariosOrdenadosPorFecha();
            var usuariosActivos = usuarioService.obtenerUsuariosActivos();
            var usuariosInactivos = usuarioService.obtenerUsuariosInactivos();
            
            model.addAttribute("usuariosOrdenados", usuariosOrdenados);
            model.addAttribute("usuariosActivos", usuariosActivos);
            model.addAttribute("usuariosInactivos", usuariosInactivos);
            model.addAttribute("pageTitle", "Consultas Avanzadas");
            
            return "consultas";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar consultas: " + e.getMessage());
            return "consultas";
        }
    }

    @PostMapping("/buscar")
    public String buscarUsuarios(@RequestParam(value = "texto", required = false) String texto,
                                 @RequestParam(value = "fechaInicio", required = false) String fechaInicio,
                                 @RequestParam(value = "fechaFin", required = false) String fechaFin,
                                 Model model) {
        
        try {
            List<?> resultados = null;
            String tipoBusqueda = "";
            
            if (texto != null && !texto.trim().isEmpty()) {
                resultados = usuarioService.buscarPorTexto(texto);
                tipoBusqueda = "texto";
                model.addAttribute("parametroBusqueda", texto);
            } else if (fechaInicio != null && fechaFin != null && 
                      !fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate inicio = LocalDate.parse(fechaInicio, formatter);
                LocalDate fin = LocalDate.parse(fechaFin, formatter);
                
                LocalDateTime fechaInicioTime = inicio.atStartOfDay();
                LocalDateTime fechaFinTime = fin.atTime(23, 59, 59);
                
                resultados = usuarioService.buscarPorRangoFechas(fechaInicioTime, fechaFinTime);
                tipoBusqueda = "rangoFechas";
                model.addAttribute("fechaInicio", fechaInicio);
                model.addAttribute("fechaFin", fechaFin);
            } else {
                resultados = usuarioService.listarUsuarios();
            }
            
            model.addAttribute("resultados", resultados);
            model.addAttribute("tipoBusqueda", tipoBusqueda);
            model.addAttribute("pageTitle", "Resultados de Búsqueda");
            
            return "consultas/buscar";
        } catch (Exception e) {
            model.addAttribute("error", "Error en la búsqueda: " + e.getMessage());
            return "consultas";
        }
    }
    
    @GetMapping("/por-rol")
    public String usuariosPorRol(@RequestParam("rol") String rolNombre, Model model) {
        try {
            var usuarios = usuarioService.findByRolNombre(rolNombre);
            
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("rolNombre", rolNombre);
            model.addAttribute("pageTitle", "Usuarios por Rol: " + rolNombre);
            
            return "consultas/por-rol";
        } catch (Exception e) {
            model.addAttribute("error", "Error al buscar por rol: " + e.getMessage());
            return "consultas";
        }
    }
}