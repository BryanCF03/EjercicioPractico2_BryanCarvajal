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
    
    // CONSULTAS DERIVADAS (Requisito D del PDF)
    
    // 1. Buscar usuarios por rol (Requisito: Buscar usuarios por rol)
    List<Usuario> findByRol(Rol rol);
    
    // 2. Buscar usuarios creados en un rango de fechas (Requisito: Buscar usuarios creados en un rango de fechas)
    List<Usuario> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // 3. Buscar usuarios por coincidencia parcial en correo o nombre (Requisito: Buscar usuarios por coincidencia parcial en correo o nombre)
    List<Usuario> findByEmailContainingIgnoreCaseOrNombreContainingIgnoreCase(String email, String nombre);
    
    // 4. Contar usuarios activos (Requisito: Contar usuarios activos vs inactivos)
    int countByActivoTrue();
    int countByActivoFalse();
    
    // 5. Obtener usuarios ordenados por fecha de creación (Requisito: Obtener usuarios ordenados por fecha de creación)
    List<Usuario> findAllByOrderByFechaCreacionDesc();
    
    // 6. Buscar usuario por email (para login)
    Usuario findByEmail(String email);
    
    // 7. Contar usuarios por rol
    int countByRol(Rol rol);
    
    // 8. Buscar por nombre que contenga texto
    List<Usuario> findByNombreContainingIgnoreCase(String texto);
    
    // 9. Buscar por apellido que contenga texto
    List<Usuario> findByApellidoContainingIgnoreCase(String texto);
    
    // 10. Buscar usuarios activos
    List<Usuario> findByActivoTrue();
    
    // 11. Buscar usuarios inactivos
    List<Usuario> findByActivoFalse();
    
    // CONSULTA PERSONALIZADA CON JPQL (Ejemplo adicional)
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Usuario> buscarPorNombreOApellido(@Param("texto") String texto);
    
    // Consulta para reporte estadístico
    @Query("SELECT r.nombre, COUNT(u) FROM Usuario u JOIN u.rol r GROUP BY r.nombre")
    List<Object[]> contarUsuariosPorRol();
    
    // Consulta para usuarios recientes (últimos 7 días)
    @Query("SELECT u FROM Usuario u WHERE u.fechaCreacion >= :fechaInicio ORDER BY u.fechaCreacion DESC")
    List<Usuario> findUsuariosRecientes(@Param("fechaInicio") LocalDateTime fechaInicio);
}