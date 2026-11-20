package com.zaror.microservicioalertas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/alertas", "/alertas/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/alertas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/alertas/*").hasAnyRole("ENCARGADO", "DIRECTIVA")
                        .requestMatchers(HttpMethod.DELETE, "/alertas/*").hasAnyRole("ENCARGADO", "DIRECTIVA")
                        .requestMatchers(HttpMethod.GET, "/fallas", "/comunicados").authenticated()
                        .requestMatchers(HttpMethod.POST, "/fallas").hasRole("ENCARGADO")
                        .requestMatchers(HttpMethod.PUT, "/fallas/*/resuelta").hasRole("ENCARGADO")
                        .requestMatchers(HttpMethod.POST, "/comunicados").hasRole("DIRECTIVA")
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").authenticated()
                        .anyRequest().denyAll())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
