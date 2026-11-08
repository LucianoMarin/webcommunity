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
    public String guardarEvento(@ModelAttribute Evento evento) {
        eventoService.guardar(evento);
        return "redirect:/admin";
    }

    @GetMapping("/evento/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {
        eventoService.eliminar(id);
        return "redirect:/admin";
    }

    @GetMapping("/usuario/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin";
    }

    @PostMapping("/usuario/rol")
    public String cambiarRol(@RequestParam Long usuarioId, @RequestParam String rol) {
        usuarioService.cambiarRol(usuarioId, rol);
        return "redirect:/admin";
    }
}