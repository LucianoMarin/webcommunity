package com.gamercomunity.dto;

import jakarta.validation.constraints.*;
import java.nio.charset.StandardCharsets;

public class RegistroForm {
    @NotBlank @Pattern(regexp="[a-zA-Z0-9_]{3,30}", message="Usa entre 3 y 30 letras, números o guion bajo")
    private String username;
    @NotBlank @Email @Size(max=150) private String email;
    @NotBlank @Size(min=12,max=72,message="Usa entre 12 y 72 caracteres") private String password;
    @AssertTrue(message="La contraseña debe ocupar como máximo 72 bytes UTF-8")
    public boolean isPasswordBytesValid() { return password==null || password.getBytes(StandardCharsets.UTF_8).length<=72; }
    public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
}
