package com.gamercomunity.controllers;
import com.gamercomunity.dto.RegistroForm;
import com.gamercomunity.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UsuarioService usuarios;
    public AuthController(UsuarioService usuarios){this.usuarios=usuarios;}
    @GetMapping("/login") public String login(){return "login";}
    @GetMapping("/registro") public String registro(Model model){model.addAttribute("usuario",new RegistroForm());return "registro";}
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") RegistroForm form, BindingResult errors, Model model){
        if(errors.hasErrors()){form.setPassword(null);return "registro";}
        try { usuarios.registrar(form); return "redirect:/login?registrado"; }
        catch(IllegalArgumentException | org.springframework.dao.DataIntegrityViolationException e){
            form.setPassword(null);
            model.addAttribute("error","No se pudo crear la cuenta con estos datos. Usa otro usuario o correo.");
            return "registro";
        }
    }
}
