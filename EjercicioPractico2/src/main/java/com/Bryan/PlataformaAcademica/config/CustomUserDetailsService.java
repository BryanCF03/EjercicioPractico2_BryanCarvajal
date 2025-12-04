package com.Bryan.PlataformaAcademica.config;

import com.Bryan.PlataformaAcademica.domain.Usuario;
import com.Bryan.PlataformaAcademica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }
        
        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + email);
        }
        
        // Obtener el nombre del rol
        String rolNombre = "USER";
        if (usuario.getRol() != null && usuario.getRol().getNombre() != null) {
            rolNombre = usuario.getRol().getNombre().toUpperCase();
        }
        
        // Crear lista de autoridades
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + rolNombre));
        
        return new User(
            usuario.getEmail(),
            usuario.getPassword(),
            usuario.getActivo(), // account enabled
            true, // account non-expired
            true, // credentials non-expired
            true, // account non-locked
            authorities
        );
    }
}