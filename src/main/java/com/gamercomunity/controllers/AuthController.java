package com.gamercomunity.controllers;

import com.gamercomunity.models.Usuario;
import com.gamercomunity.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute Usuario usuario, Model model) {
        try {
            if (usuario.getUsername() == null || !usuario.getUsername().matches("^[a-zA-Z0-9_]{3,20}$")) {
                throw new RuntimeException(
                        "El nombre de usuario solo puede tener letras, números y guion bajo (3 a 20 caracteres)");
            }
            usuarioService.registrar(usuario);
            return "redirect:/login?registrado";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "registro";
        }
    }
}