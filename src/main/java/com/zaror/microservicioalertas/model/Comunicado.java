package com.zaror.microservicioalertas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "comunicados")
@Data
public class Comunicado {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String titulo;
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String mensaje;
    private String autor;
    private Long fecha;
}
