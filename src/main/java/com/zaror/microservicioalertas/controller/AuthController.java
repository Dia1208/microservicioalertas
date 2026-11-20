package com.zaror.microservicioalertas.controller;

import com.zaror.microservicioalertas.dto.PerfilUsuario;
import com.zaror.microservicioalertas.dto.RegistroUsuarioRequest;
import com.zaror.microservicioalertas.service.ApiUsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final ApiUsuarioService usuarios;

    public AuthController(ApiUsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public PerfilUsuario registrar(@Valid @RequestBody RegistroUsuarioRequest solicitud) {
        return usuarios.registrarVecino(solicitud);
    }

    @GetMapping("/me")
    public PerfilUsuario perfil(Authentication autenticacion) {
        return usuarios.perfil(autenticacion.getName());
    }
}
