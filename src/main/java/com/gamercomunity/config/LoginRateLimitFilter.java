package com.gamercomunity.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.filter.OncePerRequestFilter;

/** Límite local por dirección de conexión; no confiar en X-Forwarded-For del cliente. */
public class LoginRateLimitFilter extends OncePerRequestFilter {
    private final Map<String,Window> windows=new HashMap<>();
    private record Window(long start,int attempts) {}
    private synchronized boolean allow(String address){
        long now=System.currentTimeMillis();
        windows.entrySet().removeIf(e->now-e.getValue().start()>60000);
        Window previous=windows.get(address);
        if(previous==null){if(windows.size()>=10000)return false;previous=new Window(now,0);}
        if(previous.attempts()>=10)return false;
        windows.put(address,new Window(previous.start(),previous.attempts()+1));return true;
    }
    @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
        if("POST".equals(request.getMethod()) && "/login".equals(request.getRequestURI().substring(request.getContextPath().length())) && !allow(request.getRemoteAddr())){
            response.setStatus(429);response.setHeader("Retry-After","60");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<!DOCTYPE html><html lang='es'><title>Espera antes de volver a intentar</title><p>Demasiados intentos. Espera un minuto y vuelve a iniciar sesión.</p><a href='/login'>Volver al login</a></html>");return;
        }
        chain.doFilter(request,response);
    }
}
