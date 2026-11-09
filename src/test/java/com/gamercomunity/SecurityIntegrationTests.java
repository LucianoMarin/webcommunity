package com.gamercomunity;

import com.gamercomunity.models.*;
import com.gamercomunity.repository.*;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class SecurityIntegrationTests {
    @Autowired MockMvc mvc;
    @Autowired UsuarioRepository usuarios;
    @Autowired RolRepository roles;
    @Autowired EventoRepository eventos;
    @Autowired PasswordEncoder encoder;
    Long eventId, adminId;
    @BeforeEach void seed(){
        usuarios.deleteAll(); eventos.deleteAll();
        Rol user=roles.findByNombre("ROLE_USER").orElseGet(()->roles.save(new Rol("ROLE_USER")));
        Rol admin=roles.findByNombre("ROLE_ADMIN").orElseGet(()->roles.save(new Rol("ROLE_ADMIN")));
        for(String name:new String[]{"admin","usuario","usuario2"}){
            Usuario u=new Usuario();u.setUsername(name);u.setEmail(name+"@example.com");
            u.setPassword(encoder.encode("FraseSeguraDePrueba2026!"));u.setAvatarUrl("/images/avatar.svg");
            u.setRoles(name.equals("admin")?Set.of(user,admin):Set.of(user));u=usuarios.save(u);
            if(name.equals("admin"))adminId=u.getId();
        }
        Evento e=new Evento();e.setNombre("Torneo de prueba");e.setCategoria("Esports");
        e.setDireccion("Santiago");e.setFechaHora(LocalDateTime.now().plusDays(1));eventId=eventos.save(e).getId();
    }
    @Test void inicioPublico() throws Exception {mvc.perform(get("/home")).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Torneo de prueba")));}
    @Test void rutaSolicitadaPublica() throws Exception {mvc.perform(get("/recetas")).andExpect(status().isOk());}
    @Test void busquedaPublica() throws Exception {mvc.perform(get("/eventos").param("categoria","Esports")).andExpect(status().isOk());}
    @Test void detallePrivado() throws Exception {mvc.perform(get("/eventos/"+eventId)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));}
    @Test void usuarioVeDetalle() throws Exception {mvc.perform(get("/eventos/"+eventId).with(user("usuario"))).andExpect(status().isOk());}
    @Test void usuarioNoEsAdmin() throws Exception {mvc.perform(get("/admin").with(user("usuario"))).andExpect(status().isForbidden());}
    @Test void adminVePanel() throws Exception {mvc.perform(get("/admin").with(user("admin").roles("ADMIN"))).andExpect(status().isOk());}
    @Test void noEliminaConGet() throws Exception {mvc.perform(get("/admin/evento/eliminar/"+eventId).with(user("admin").roles("ADMIN"))).andExpect(status().isMethodNotAllowed());assertThat(eventos.existsById(eventId)).isTrue();}
    @Test void noEliminaSinCsrf() throws Exception {mvc.perform(post("/admin/evento/eliminar/"+eventId).with(user("admin").roles("ADMIN"))).andExpect(status().isForbidden());assertThat(eventos.existsById(eventId)).isTrue();}
    @Test void usuarioNoEliminaConCsrf() throws Exception {mvc.perform(post("/admin/evento/eliminar/"+eventId).with(user("usuario")).with(csrf())).andExpect(status().isForbidden());}
    @Test void adminEliminaConCsrf() throws Exception {mvc.perform(post("/admin/evento/eliminar/"+eventId).with(user("admin").roles("ADMIN")).with(csrf())).andExpect(redirectedUrl("/admin"));assertThat(eventos.existsById(eventId)).isFalse();}
    @Test void registroIgnoraIdYRolesAtacante() throws Exception {
        String hash=usuarios.findById(adminId).orElseThrow().getPassword();
        mvc.perform(post("/registro").with(csrf()).param("username","nuevo_usuario").param("email","nuevo@example.com")
            .param("password","FraseMuySeguraParaRegistro!").param("id",adminId.toString()).param("roles[0].nombre","ROLE_ADMIN"))
            .andExpect(redirectedUrl("/login?registrado"));
        assertThat(usuarios.findById(adminId).orElseThrow().getPassword()).isEqualTo(hash);
        Usuario nuevo=usuarios.findByUsername("nuevo_usuario").orElseThrow();
        assertThat(nuevo.getId()).isNotEqualTo(adminId);assertThat(nuevo.getRoles()).extracting(Rol::getNombre).containsExactly("ROLE_USER");
        assertThat(encoder.matches("FraseMuySeguraParaRegistro!",nuevo.getPassword())).isTrue();
    }
    @Test void registroRechazaClaveDebil() throws Exception {mvc.perform(post("/registro").with(csrf()).param("username","nuevo").param("email","nuevo@example.com").param("password","123")).andExpect(status().isOk()).andExpect(view().name("registro"));assertThat(usuarios.existsByUsername("nuevo")).isFalse();}
    @Test void registroRechazaSinCsrf() throws Exception {mvc.perform(post("/registro")).andExpect(status().isForbidden());}
    @Test void perfilNoAceptaUrlExterna() throws Exception {mvc.perform(post("/perfil").with(user("usuario")).with(csrf()).param("avatarUrl","https://evil.example/a.svg")).andExpect(view().name("perfil"));assertThat(usuarios.findByUsername("usuario").orElseThrow().getAvatarUrl()).isEqualTo("/images/avatar.svg");}
    @Test void eventoInexistente404() throws Exception {mvc.perform(get("/eventos/999999").with(user("usuario"))).andExpect(status().isNotFound());}
    @Test void cabecerasSeguridad() throws Exception {mvc.perform(get("/home")).andExpect(header().string("X-Frame-Options","DENY")).andExpect(header().string("X-Content-Type-Options","nosniff")).andExpect(header().exists("Content-Security-Policy"));}
    @Test void tresUsuariosPuedenAutenticarse() throws Exception {for(String name:new String[]{"admin","usuario","usuario2"})mvc.perform(post("/login").with(csrf()).param("username",name).param("password","FraseSeguraDePrueba2026!")).andExpect(redirectedUrl("/home"));}
    @Test void noAutoEliminacionAdmin() throws Exception {mvc.perform(post("/admin/usuario/eliminar/"+adminId).with(user("admin").roles("ADMIN")).with(csrf())).andExpect(redirectedUrl("/admin?protegido"));assertThat(usuarios.existsById(adminId)).isTrue();}
    @Test void guardaEventoValido() throws Exception {mvc.perform(post("/admin/evento/guardar").with(user("admin").roles("ADMIN")).with(csrf()).param("nombre","Nuevo torneo").param("fechaHora","2027-02-01T18:00")).andExpect(redirectedUrl("/admin"));assertThat(eventos.count()).isEqualTo(2);}
    @Test void edicionConservaFechaIso() throws Exception {mvc.perform(get("/admin/evento/editar/"+eventId).with(user("admin").roles("ADMIN"))).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("T")));mvc.perform(post("/admin/evento/guardar").with(user("admin").roles("ADMIN")).with(csrf()).param("id",eventId.toString()).param("nombre","Editado").param("fechaHora","2027-02-01T18:00")).andExpect(redirectedUrl("/admin"));assertThat(eventos.findById(eventId).orElseThrow().getNombre()).isEqualTo("Editado");}
    @Test void limitaIntentosLogin() throws Exception {for(int i=0;i<10;i++)mvc.perform(post("/login").with(csrf()).with(r->{r.setRemoteAddr("127.0.0.99");return r;}).param("username","inexistente").param("password","incorrecta")).andExpect(status().is3xxRedirection());mvc.perform(post("/login").with(csrf()).with(r->{r.setRemoteAddr("127.0.0.99");return r;})).andExpect(status().isTooManyRequests()).andExpect(header().exists("Retry-After"));}
    @Test void escapaHtmlEnNombreEvento() throws Exception {Evento e=eventos.findById(eventId).orElseThrow();e.setNombre("<script>alert(1)</script>");eventos.save(e);mvc.perform(get("/home")).andExpect(content().string(org.hamcrest.Matchers.containsString("&lt;script&gt;"))).andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("<script>alert(1)</script>"))));}
    @Test void errorLoginUnicodeControlado() throws Exception {mvc.perform(post("/login").with(csrf()).param("username","😃\r\nforjado").param("password","incorrecta")).andExpect(redirectedUrl("/login?error"));}
}
