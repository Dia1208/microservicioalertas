package com.zaror.microservicioalertas.repository;

import com.zaror.microservicioalertas.model.ApiUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUsuarioRepository extends JpaRepository<ApiUsuario, Long> {
    Optional<ApiUsuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
