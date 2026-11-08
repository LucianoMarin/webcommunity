package com.gamercomunity.controllers;

import com.gamercomunity.models.Evento;
import com.gamercomunity.service.EventoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fecha,
            @RequestParam(required = false) String ubicacion,
            Model model) {

        List<Evento> eventos = eventoService.buscar(categoria, fecha, ubicacion);
        model.addAttribute("eventos", eventos);
        model.addAttribute("categoria", categoria);
        model.addAttribute("fecha", fecha);
        model.addAttribute("ubicacion", ubicacion);
        return "eventos/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("evento", eventoService.obtenerPorId(id));
        return "eventos/detalle";
    }
}