
package com.zaror.microservicioalertas.dto;

import lombok.Data;

@Data
public class AlertaDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String ubicacion;
    private String estado;

}
