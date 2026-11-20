package com.zaror.microservicioalertas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "fallas")
@Data
public class Falla {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String componente;
    @NotBlank private String detalle;
    @NotBlank private String prioridad;
    private String reportadoPor;
    private Long fecha;
    private boolean resuelta;
}
