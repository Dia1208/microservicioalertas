package com.zaror.microservicioalertas.config;

import com.zaror.microservicioalertas.model.ApiUsuario;
import com.zaror.microservicioalertas.repository.ApiUsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DemoUsers {
    @Bean
    CommandLineRunner cuentasDemostracion(
            ApiUsuarioRepository usuarios,
            PasswordEncoder encoder,
            @Value("${DEMO_PASSWORD:Duoc.2026@}") String clave) {
        return args -> {
            if (clave.isBlank()) return;
            guardar(usuarios, encoder, "alejandro@encargado.cl", "ENCARGADO", clave);
            guardar(usuarios, encoder, "alejandro@directiva.cl", "DIRECTIVA", clave);
        };
    }

    private void guardar(ApiUsuarioRepository usuarios, PasswordEncoder encoder,
                         String correo, String rol, String clave) {
        ApiUsuario usuario = usuarios.findByCorreo(correo).orElseGet(ApiUsuario::new);
        usuario.setCorreo(correo);
        usuario.setClaveHash(encoder.encode(clave));
        usuario.setRol(rol);
        usuario.setNombre(rol.equals("DIRECTIVA") ? "Alejandro Directiva" : "Alejandro Encargado");
        usuarios.save(usuario);
    }
}
