package com.gamercomunity.dto;
import jakarta.validation.constraints.*;
public class PerfilForm {
    @NotBlank @Pattern(regexp="/images/avatar[.]svg",message="Selecciona el avatar local permitido") private String avatarUrl="/images/avatar.svg";
    @Size(max=1000) private String juegosInteres;
    private boolean notificarEmail, notificarPush;
    public String getAvatarUrl(){return avatarUrl;} public void setAvatarUrl(String v){avatarUrl=v;}
    public String getJuegosInteres(){return juegosInteres;} public void setJuegosInteres(String v){juegosInteres=v;}
    public boolean isNotificarEmail(){return notificarEmail;} public void setNotificarEmail(boolean v){notificarEmail=v;}
    public boolean isNotificarPush(){return notificarPush;} public void setNotificarPush(boolean v){notificarPush=v;}
}
