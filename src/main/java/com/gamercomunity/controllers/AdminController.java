package com.gamercomunity.controllers;

import com.gamercomunity.models.Evento;
import com.gamercomunity.service.EventoService;
import com.gamercomunity.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventoService eventoService;
    private final UsuarioService usuarioService;

    public AdminController(EventoService eventoService, UsuarioService usuarioService) {
        this.eventoService = eventoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String panelAdmin(Model model) {
        model.addAttribute("eventos", eventoService.listarProximos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/panel";
    }

    @GetMapping("/evento/nuevo")
    public String nuevoEvento(Model model) {
        model.addAttribute("evento", new Evento());
        return "admin/formulario-evento";
    }

    @GetMapping("/evento/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model) {
        model.addAttribute("evento", eventoService.obtenerPorId(id));
        return "admin/formulario-evento";
    }

    @PostMapping("/evento/guardar")
    public String guardarEvento(@jakarta.validation.Valid @ModelAttribute Evento evento,
            org.springframework.validation.BindingResult errors) {
        if (errors.hasErrors()) return "admin/formulario-evento";
        eventoService.guardar(evento);
        return "redirect:/admin";
    }

    @PostMapping("/evento/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {
        eventoService.eliminar(id);
        return "redirect:/admin";
    }

    @PostMapping("/usuario/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, org.springframework.security.core.Authentication auth) {
        if (usuarioService.obtenerPorUsername(auth.getName()).getId().equals(id))
            return "redirect:/admin?protegido";
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin";
    }

    @PostMapping("/usuario/rol")
    public String cambiarRol(@RequestParam Long usuarioId, @RequestParam String rol, org.springframework.security.core.Authentication auth) {
        if (usuarioService.obtenerPorUsername(auth.getName()).getId().equals(usuarioId))
            return "redirect:/admin?protegido";
        usuarioService.cambiarRol(usuarioId, rol);
        return "redirect:/admin";
    }
}
