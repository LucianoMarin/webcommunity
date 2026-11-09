package com.gamercomunity.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.*;
import org.springframework.stereotype.Component;

@Component
public class SecurityAuditListener {
    private static final Logger LOG=LoggerFactory.getLogger(SecurityAuditListener.class);
    private String safe(String name){if(name==null)return "desconocido";String cleaned=name.replaceAll("[^a-zA-Z0-9_@.-]","_");return cleaned.substring(0,Math.min(cleaned.length(),100));}
    @EventListener public void success(AuthenticationSuccessEvent event){LOG.info("AUTH_SUCCESS usuario={}",safe(event.getAuthentication().getName()));}
    @EventListener public void failure(AbstractAuthenticationFailureEvent event){LOG.warn("AUTH_FAILURE usuario={}",safe(event.getAuthentication().getName()));}
}
