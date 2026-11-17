
package com.zaror.microservicioalertas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "alertas")
@Data
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private String tipo;
    private String ubicacion;
    private String estado;
}
