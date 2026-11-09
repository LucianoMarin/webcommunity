package com.gamercomunity.controllers;

import com.gamercomunity.models.Usuario;
import com.gamercomunity.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String verPerfil(Authentication auth, Model model) {
        Usuario usuario = usuarioService.obtenerPorUsername(auth.getName());
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping
    public String actualizarPerfil(Authentication auth, @ModelAttribute Usuario datos) {
        usuarioService.actualizarPerfil(auth.getName(), datos);
        return "redirect:/perfil?actualizado";
    }
}