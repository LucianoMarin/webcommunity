package com.gamercomunity.config;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new LoginRateLimitFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session.maximumSessions(-1).sessionRegistry(sessionRegistry()))
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/error").permitAll()
            .requestMatchers(HttpMethod.GET, "/", "/home", "/recetas", "/eventos", "/login", "/registro").permitAll()
            .requestMatchers(HttpMethod.POST, "/registro").permitAll()
            .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/home", false)
                .failureUrl("/login?error").permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self'; font-src 'self'; connect-src 'self'; object-src 'none'; base-uri 'self'; frame-ancestors 'none'; form-action 'self'"))
                .frameOptions(frame -> frame.deny())
                .referrerPolicy(ref -> ref.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                .permissionsPolicyHeader(p -> p.policy("camera=(), microphone=(), geolocation=()")));
        return http.build();
    }
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(12); }
    @Bean org.springframework.security.core.session.SessionRegistry sessionRegistry() {return new org.springframework.security.core.session.SessionRegistryImpl();}
    @Bean static org.springframework.security.web.session.HttpSessionEventPublisher httpSessionEventPublisher() {return new org.springframework.security.web.session.HttpSessionEventPublisher();}
}
