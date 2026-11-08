package com.gamercomunity.config;

import com.gamercomunity.models.Usuario;
import com.gamercomunity.models.Rol;
import com.gamercomunity.repository.UsuarioRepository;
import com.gamercomunity.repository.RolRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsuarios(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Buscar roles existentes
            Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                    .orElseThrow(() ->
                            new RuntimeException("No existe ROLE_USER"));

            Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                    .orElseThrow(() ->
                            new RuntimeException("No existe ROLE_ADMIN"));

            // Crear ADMIN
            if (!usuarioRepository.existsByUsername("admin")) {

                Usuario admin = new Usuario();

                admin.setUsername("admin");
                admin.setEmail("gamer3@example.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setAvatarUrl(
                        "https://api.dicebear.com/7.x/bottts/svg?seed=admin");
                admin.setJuegosInteres(
                        "Minecraft, Fortnite, Apex Legends");
                admin.setNotificarEmail(true);
                admin.setNotificarPush(true);

                Set<Rol> rolesAdmin = new HashSet<>();
                rolesAdmin.add(rolUser);
                rolesAdmin.add(rolAdmin);

                admin.setRoles(rolesAdmin);

                usuarioRepository.save(admin);
            }

            // Crear LMARIN
            if (!usuarioRepository.existsByUsername("usuario")) {

                Usuario lmarin = new Usuario();

                lmarin.setUsername("usuario");
                lmarin.setEmail("gamer1@example.com");
                lmarin.setPassword(passwordEncoder.encode("usuario"));
                lmarin.setAvatarUrl(
                        "https://api.dicebear.com/7.x/bottts/svg?seed=gamer1");
                lmarin.setJuegosInteres(
                        "Valorant, League of Legends, CS2");
                lmarin.setNotificarEmail(true);
                lmarin.setNotificarPush(false);

                lmarin.setRoles(Set.of(rolUser));

                usuarioRepository.save(lmarin);
            }

            // Crear USUARIO
            if (!usuarioRepository.existsByUsername("usuario2")) {

                Usuario usuario = new Usuario();

                usuario.setUsername("usuario2");
                usuario.setEmail("gamer2@example.com");
                usuario.setPassword(passwordEncoder.encode("usuario2"));
                usuario.setAvatarUrl(
                        "https://api.dicebear.com/7.x/bottts/svg?seed=gamer2");
                usuario.setJuegosInteres(
                        "Dota 2, CS2, Elden Ring");
                usuario.setNotificarEmail(false);
                usuario.setNotificarPush(true);

                usuario.setRoles(Set.of(rolUser));

                usuarioRepository.save(usuario);
            }

            System.out.println("Usuarios y roles inicializados correctamente.");
        };
    }
}