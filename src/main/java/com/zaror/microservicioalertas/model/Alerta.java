
package com.zaror.microservicioalertas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "alertas")
@Data
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String titulo;
    @NotBlank private String descripcion;
    @NotBlank private String tipo;
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String ubicacion;
    @NotBlank private String estado;
    private String autor;
    private Long fecha;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String fotoBase64;
}
