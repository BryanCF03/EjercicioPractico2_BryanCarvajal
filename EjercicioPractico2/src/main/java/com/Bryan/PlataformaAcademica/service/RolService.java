package com.Bryan.PlataformaAcademica.service;

import com.Bryan.PlataformaAcademica.domain.Rol;
import java.util.List;

public interface RolService {
    List<Rol> listarRoles();
    Rol obtenerPorId(Long id);
    Rol guardar(Rol rol);
    void eliminar(Long id);
    Rol findByNombre(String nombre);
    boolean existePorNombre(String nombre);
}