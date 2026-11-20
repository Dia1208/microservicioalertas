
package com.zaror.microservicioalertas.controller;

import com.zaror.microservicioalertas.model.Alerta;
import com.zaror.microservicioalertas.service.AlertaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.security.Principal;
import java.util.Base64;

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
    public Alerta guardarAlerta(@Valid @RequestBody Alerta alerta, Principal principal) {
        if (alerta.getFotoBase64() != null) {
            if (alerta.getFotoBase64().length() > 1_000_000) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fotografía demasiado grande");
            }
            try {
                Base64.getDecoder().decode(alerta.getFotoBase64());
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fotografía inválida");
            }
        }
        alerta.setId(null);
        alerta.setAutor(principal.getName());
        alerta.setFecha(System.currentTimeMillis());
        alerta.setEstado("Activa");
        return alertaService.guardarAlerta(alerta);
    }

    // Actualizar una alerta
    @PutMapping("/{id}")
    public Alerta actualizarAlerta(
            @PathVariable Long id,
            @Valid @RequestBody Alerta alerta) {

        return alertaService.actualizarAlerta(id, alerta);
    }

    // Eliminar una alerta
    @DeleteMapping("/{id}")
    public void eliminarAlerta(@PathVariable Long id) {
        alertaService.eliminarAlerta(id);
    }
}
