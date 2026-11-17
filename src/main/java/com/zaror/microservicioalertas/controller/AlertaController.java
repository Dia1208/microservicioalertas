
package com.zaror.microservicioalertas.controller;

import com.zaror.microservicioalertas.model.Alerta;
import com.zaror.microservicioalertas.service.AlertaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    // Listar todas las alertas
    @GetMapping
    public List<Alerta> listarAlertas() {
        return alertaService.listarAlertas();
    }

    // Buscar una alerta por ID
    @GetMapping("/{id}")
    public Alerta buscarAlerta(@PathVariable Long id) {
        return alertaService.buscarAlerta(id);
    }

    // Guardar una alerta
    @PostMapping
    public Alerta guardarAlerta(@RequestBody Alerta alerta) {
        return alertaService.guardarAlerta(alerta);
    }

    // Actualizar una alerta
    @PutMapping("/{id}")
    public Alerta actualizarAlerta(
            @PathVariable Long id,
            @RequestBody Alerta alerta) {

        return alertaService.actualizarAlerta(id, alerta);
    }

    // Eliminar una alerta
    @DeleteMapping("/{id}")
    public void eliminarAlerta(@PathVariable Long id) {
        alertaService.eliminarAlerta(id);
    }
}
