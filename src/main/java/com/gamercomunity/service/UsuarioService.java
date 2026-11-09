package com.gamercomunity.service;

import com.gamercomunity.models.Rol;
import com.gamercomunity.models.Usuario;
import com.gamercomunity.repository.RolRepository;
import com.gamercomunity.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final org.springframework.security.core.session.SessionRegistry sessions;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder,
                          org.springframework.security.core.session.SessionRegistry sessions) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessions = sessions;
    }

    public Usuario registrar(com.gamercomunity.dto.RegistroForm datos) {
        Usuario usuario = new Usuario();
        usuario.setUsername(datos.getUsername());
        usuario.setEmail(datos.getEmail().toLowerCase(java.util.Locale.ROOT));
        usuario.setPassword(datos.getPassword());
        usuario.setAvatarUrl("/images/avatar.svg");
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("No se pudo crear la cuenta con estos datos. Usa otro usuario o correo.");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("No se pudo crear la cuenta con estos datos. Usa otro usuario o correo.");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
            .orElseGet(() -> rolRepository.save(new Rol("ROLE_USER")));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolUser);
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarPerfil(String username, com.gamercomunity.dto.PerfilForm datos) {
        Usuario existente = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existente.setAvatarUrl(datos.getAvatarUrl());
        existente.setJuegosInteres(datos.getJuegosInteres());
        existente.setNotificarEmail(datos.isNotificarEmail());
        existente.setNotificarPush(datos.isNotificarPush());

        return usuarioRepository.save(existente);
    }

    public Usuario obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no disponible"));
        revocarSesiones(usuario.getUsername());
        usuarioRepository.deleteById(id);
    }

    public void cambiarRol(Long usuarioId, String nombreRol) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Rol rol = rolRepository.findByNombre(nombreRol)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        usuario.setRoles(roles);
        
        usuarioRepository.save(usuario);
        revocarSesiones(usuario.getUsername());
    }
    private void revocarSesiones(String username) {
        for(Object principal:sessions.getAllPrincipals()) {
            if(principal instanceof org.springframework.security.core.userdetails.UserDetails user && user.getUsername().equals(username)) {
                sessions.getAllSessions(principal,false).forEach(org.springframework.security.core.session.SessionInformation::expireNow);
            }
        }
    }
}
