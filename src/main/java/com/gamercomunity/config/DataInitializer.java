package com.gamercomunity.config;

import com.gamercomunity.models.*;
import com.gamercomunity.repository.*;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnProperty(name="app.seed.enabled", havingValue="true")
public class DataInitializer {
    @Bean
    CommandLineRunner datosIniciales(UsuarioRepository usuarios, RolRepository roles,
            EventoRepository eventos, PasswordEncoder encoder, Environment env) {
        return args -> {
            Rol user = roles.findByNombre("ROLE_USER").orElseGet(() -> roles.save(new Rol("ROLE_USER")));
            Rol admin = roles.findByNombre("ROLE_ADMIN").orElseGet(() -> roles.save(new Rol("ROLE_ADMIN")));
            String[] names = {"admin", "usuario", "usuario2"};
            for (String name : names) {
                if (usuarios.existsByUsername(name)) continue;
                String password = env.getRequiredProperty("DEMO_"+name.toUpperCase()+"_PASSWORD");
                if (password.length()<12 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72)
                    throw new IllegalStateException("Las claves de demostración deben tener 12 caracteres y máximo 72 bytes");
                Usuario u = new Usuario();
                u.setUsername(name); u.setEmail(name+"@example.com");
                u.setPassword(encoder.encode(password));
                u.setAvatarUrl("/images/avatar.svg");
                u.setJuegosInteres("Valorant, Minecraft, CS2");
                u.setRoles(name.equals("admin") ? Set.of(user,admin) : Set.of(user));
                usuarios.save(u);
            }
            if (eventos.count()==0) {
                String[][] data = {{"Torneo Nacional Valorant", "Esports", "Santiago", "Competencia de equipos amateur y profesionales."},
                    {"Expo Gaming", "Expo", "Viña del Mar", "Consolas, cosplay y desarrolladores chilenos."},
                    {"LAN Party Retro", "Comunidad", "Concepción", "Encuentro de clásicos y coleccionistas."},
                    {"Encuentro Indie", "Desarrollo", "Santiago", "Prototipos y charlas de desarrollo de videojuegos."}};
                for(int i=0;i<data.length;i++) {
                    Evento e=new Evento(); e.setNombre(data[i][0]); e.setCategoria(data[i][1]);
                    e.setDireccion(data[i][2]); e.setDescripcion(data[i][3]);
                    e.setFechaHora(LocalDateTime.now().plusDays(7L*(i+1)).withHour(18).withMinute(0).withSecond(0).withNano(0));
                    e.setOrganizadores("Comunidad Gamer Chile"); e.setExpositoresPrincipales("Equipos y estudios invitados");
                    e.setTieneLocomocion(true); e.setTieneComidas(true); eventos.save(e);
                }
            }
        };
    }
}
