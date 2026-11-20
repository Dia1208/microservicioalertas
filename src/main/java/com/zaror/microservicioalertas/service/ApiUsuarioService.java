package com.zaror.microservicioalertas.service;

import com.zaror.microservicioalertas.dto.PerfilUsuario;
import com.zaror.microservicioalertas.dto.RegistroUsuarioRequest;
import com.zaror.microservicioalertas.model.ApiUsuario;
import com.zaror.microservicioalertas.repository.ApiUsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class ApiUsuarioService implements UserDetailsService {
    private final ApiUsuarioRepository usuarios;
    private final PasswordEncoder encoder;

    public ApiUsuarioService(ApiUsuarioRepository usuarios, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.encoder = encoder;
    }

    public PerfilUsuario registrarVecino(RegistroUsuarioRequest solicitud) {
        String correo = normalizar(solicitud.correo());
        if (usuarios.existsByCorreo(correo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        ApiUsuario usuario = new ApiUsuario();
        usuario.setCorreo(correo);
        usuario.setClaveHash(encoder.encode(solicitud.clave()));
        usuario.setRol("VECINO");
        usuario.setNombre(solicitud.nombre());
        usuario.setTelefono(solicitud.telefono());
        usuario.setDireccion(solicitud.direccion());
        try {
            usuarios.saveAndFlush(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        return new PerfilUsuario(correo, usuario.getRol(), usuario.getNombre(),
                usuario.getTelefono(), usuario.getDireccion());
    }

    public PerfilUsuario perfil(String correo) {
        ApiUsuario usuario = usuarios.findByCorreo(normalizar(correo))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new PerfilUsuario(usuario.getCorreo(), usuario.getRol(), usuario.getNombre(),
                usuario.getTelefono(), usuario.getDireccion());
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        ApiUsuario usuario = usuarios.findByCorreo(normalizar(correo))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.withUsername(usuario.getCorreo())
                .password(usuario.getClaveHash())
                .roles(usuario.getRol())
                .build();
    }

    public static String normalizar(String correo) {
        return correo.trim().toLowerCase(Locale.ROOT);
    }
}
