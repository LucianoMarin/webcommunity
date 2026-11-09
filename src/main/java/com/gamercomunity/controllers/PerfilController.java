package com.gamercomunity.controllers;
import com.gamercomunity.dto.PerfilForm;
import com.gamercomunity.models.Usuario;
import com.gamercomunity.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller @RequestMapping("/perfil")
public class PerfilController {
    private final UsuarioService usuarios;
    public PerfilController(UsuarioService usuarios){this.usuarios=usuarios;}
    @GetMapping public String perfil(Authentication auth,Model model){
        Usuario u=usuarios.obtenerPorUsername(auth.getName());
        PerfilForm form=new PerfilForm(); form.setJuegosInteres(u.getJuegosInteres());
        form.setNotificarEmail(u.isNotificarEmail());form.setNotificarPush(u.isNotificarPush());
        model.addAttribute("datos",form);model.addAttribute("usuario",u);return "perfil";
    }
    @PostMapping public String actualizar(Authentication auth,@Valid @ModelAttribute("datos") PerfilForm datos,BindingResult errors,Model model){
        if(errors.hasErrors()){model.addAttribute("usuario",usuarios.obtenerPorUsername(auth.getName()));return "perfil";}
        usuarios.actualizarPerfil(auth.getName(),datos);return "redirect:/perfil?actualizado";
    }
}
