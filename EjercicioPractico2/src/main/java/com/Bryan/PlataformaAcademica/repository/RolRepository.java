package com.Bryan.PlataformaAcademica.repository;

import com.Bryan.PlataformaAcademica.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}