package com.Bryan.PlataformaAcademica.service.impl;

import com.Bryan.PlataformaAcademica.domain.Rol;
import com.Bryan.PlataformaAcademica.repository.RolRepository;
import com.Bryan.PlataformaAcademica.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    @Override
    public Rol obtenerPorId(Long id) {
        return rolRepository.findById(id).orElse(null);
    }

    @Override
    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void eliminar(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    public Rol findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return rolRepository.existsByNombre(nombre);
    }
}