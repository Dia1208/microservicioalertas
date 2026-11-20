package com.zaror.microservicioalertas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroUsuarioRequest(
        @NotBlank @Email String correo,
        @NotBlank @Size(min = 8, max = 128) String clave,
        @Size(max = 100) String nombre,
        @Size(max = 30) String telefono,
        @Size(max = 200) String direccion
) {}
