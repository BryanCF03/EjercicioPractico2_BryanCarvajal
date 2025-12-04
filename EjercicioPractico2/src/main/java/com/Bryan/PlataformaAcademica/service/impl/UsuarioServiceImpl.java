package com.Bryan.PlataformaAcademica.service.impl;

import com.Bryan.PlataformaAcademica.domain.Rol;
import com.Bryan.PlataformaAcademica.domain.Usuario;
import com.Bryan.PlataformaAcademica.repository.RolRepository;
import com.Bryan.PlataformaAcademica.repository.UsuarioRepository;
import com.Bryan.PlataformaAcademica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> findByRolNombre(String rolNombre) {
        Rol rol = rolRepository.findByNombre(rolNombre);
        if (rol != null) {
            return usuarioRepository.findByRol(rol);
        }
        return List.of();
    }

    @Override
    public List<Usuario> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return usuarioRepository.findByFechaCreacionBetween(inicio, fin);
    }

    @Override
    public List<Usuario> buscarPorTexto(String texto) {
        return usuarioRepository.findByEmailContainingIgnoreCaseOrNombreContainingIgnoreCase(texto, texto);
    }

    @Override
    public List<Usuario> obtenerUsuariosOrdenadosPorFecha() {
        return usuarioRepository.findAllByOrderByFechaCreacionDesc();
    }

    @Override
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    @Override
    public List<Usuario> obtenerUsuariosInactivos() {
        return usuarioRepository.findByActivoFalse();
    }

    @Override
    public long contarUsuariosActivos() {
        return usuarioRepository.countByActivoTrue();
    }

    @Override
    public long contarUsuariosInactivos() {
        return usuarioRepository.countByActivoFalse();
    }

    @Override
    public long contarTotalUsuarios() {
        return usuarioRepository.count();
    }

    @Override
    public List<Object[]> obtenerEstadisticasPorRol() {
        return usuarioRepository.contarUsuariosPorRol();
    }

    @Override
    public List<Usuario> obtenerUsuariosRecientes(int dias) {
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(dias);
        return usuarioRepository.findUsuariosRecientes(fechaInicio);
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email) != null;
    }

    @Override
    public boolean existeEmailYNoEsUsuarioActual(String email, Long usuarioId) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        return usuario != null && !usuario.getId().equals(usuarioId);
    }
}