package com.gamercomunity.security;

import com.gamercomunity.models.Usuario;
import com.gamercomunity.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        String[] roles = usuario.getRoles().stream()
            .map(Rol -> Rol.getNombre().replace("ROLE_", ""))
            .toArray(String[]::new);

        return User.builder()
            .username(usuario.getUsername())
            .password(usuario.getPassword())
            .roles(roles)
            .build();
    }
}