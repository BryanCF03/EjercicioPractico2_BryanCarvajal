package com.Bryan.PlataformaAcademica.repository;

import com.Bryan.PlataformaAcademica.domain.Usuario;
import com.Bryan.PlataformaAcademica.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Consultas básicas
    Usuario findByEmail(String email);
    List<Usuario> findByRol(Rol rol);
    List<Usuario> findByNombreContainingIgnoreCase(String texto);
    
    // Consultas requeridas
    List<Usuario> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Consulta simplificada para buscar por texto
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) " +
           "OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Usuario> buscarPorTexto(@Param("texto") String texto);
    
    int countByActivoTrue();
    int countByActivoFalse();
    
    List<Usuario> findAllByOrderByFechaCreacionDesc();
    
    List<Usuario> findByActivoTrue();
    List<Usuario> findByActivoFalse();
    
    // Estadísticas por rol - consulta simplificada
    @Query("SELECT r.nombre, COUNT(u) FROM Usuario u JOIN u.rol r GROUP BY r.id")
    List<Object[]> contarUsuariosPorRol();
    
    // Usuarios recientes
    @Query("SELECT u FROM Usuario u WHERE u.fechaCreacion >= :fechaInicio")
    List<Usuario> findUsuariosRecientes(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    // Contar por rol
    int countByRol(Rol rol);
}