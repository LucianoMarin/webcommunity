package com.gamercomunity.controllers;

import com.gamercomunity.service.EventoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventoService eventoService;

    public HomeController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("eventosProximos", eventoService.listarProximos());
        return "home";
    }
}