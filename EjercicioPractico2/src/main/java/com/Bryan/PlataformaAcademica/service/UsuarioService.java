package com.Bryan.PlataformaAcademica.service;

import com.Bryan.PlataformaAcademica.domain.Usuario;
import java.time.LocalDateTime;
import java.util.List;

public interface UsuarioService {
    // CRUD básico
    List<Usuario> listarUsuarios();
    Usuario obtenerPorId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);
    
    // Para login
    Usuario findByEmail(String email);
    
    // Consultas avanzadas (Requisito D)
    List<Usuario> findByRolNombre(String rolNombre);
    List<Usuario> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);
    List<Usuario> buscarPorTexto(String texto);
    List<Usuario> obtenerUsuariosOrdenadosPorFecha();
    List<Usuario> obtenerUsuariosActivos();
    List<Usuario> obtenerUsuariosInactivos();
    
    // Estadísticas
    long contarUsuariosActivos();
    long contarUsuariosInactivos();
    long contarTotalUsuarios();
    
    // Reportes
    List<Object[]> obtenerEstadisticasPorRol();
    List<Usuario> obtenerUsuariosRecientes(int dias);
    
    // Validaciones
    boolean existeEmail(String email);
    boolean existeEmailYNoEsUsuarioActual(String email, Long usuarioId);
}