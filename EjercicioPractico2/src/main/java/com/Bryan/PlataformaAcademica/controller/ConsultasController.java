package com.Bryan.PlataformaAcademica.controller;

import com.Bryan.PlataformaAcademica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/consultas")
public class ConsultasController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String consultas(Model model) {
        // Mostrar usuarios ordenados por fecha de creación (Requisito D)
        var usuariosOrdenados = usuarioService.obtenerUsuariosOrdenadosPorFecha();
        var usuariosActivos = usuarioService.obtenerUsuariosActivos();
        var usuariosInactivos = usuarioService.obtenerUsuariosInactivos();
        
        model.addAttribute("usuariosOrdenados", usuariosOrdenados);
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("usuariosInactivos", usuariosInactivos);
        model.addAttribute("pageTitle", "Consultas Avanzadas");
        
        return "consultas";
    }

    @GetMapping("/buscar")
    public String buscarUsuarios(@RequestParam(value = "texto", required = false) String texto,
                                 @RequestParam(value = "fechaInicio", required = false) 
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
                                 @RequestParam(value = "fechaFin", required = false) 
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
                                 Model model) {
        
        List<?> resultados = null;
        
        if (texto != null && !texto.trim().isEmpty()) {
            // Buscar por texto (nombre, apellido o email)
            resultados = usuarioService.buscarPorTexto(texto);
            model.addAttribute("tipoBusqueda", "texto");
            model.addAttribute("parametroBusqueda", texto);
        } else if (fechaInicio != null && fechaFin != null) {
            // Buscar por rango de fechas
            resultados = usuarioService.buscarPorRangoFechas(fechaInicio, fechaFin);
            model.addAttribute("tipoBusqueda", "rangoFechas");
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);
        }
        
        model.addAttribute("resultados", resultados);
        model.addAttribute("pageTitle", "Resultados de Búsqueda");
        
        return "consultas/buscar";
    }
    
    @GetMapping("/por-rol")
    public String usuariosPorRol(@RequestParam("rol") String rolNombre, Model model) {
        var usuarios = usuarioService.findByRolNombre(rolNombre);
        
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("rolNombre", rolNombre);
        model.addAttribute("pageTitle", "Usuarios por Rol: " + rolNombre);
        
        return "consultas/por-rol";
    }
}