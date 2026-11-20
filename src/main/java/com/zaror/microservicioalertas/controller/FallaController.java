package com.zaror.microservicioalertas.controller;

import com.zaror.microservicioalertas.model.Falla;
import com.zaror.microservicioalertas.repository.FallaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/fallas")
public class FallaController {
    private final FallaRepository repositorio;

    public FallaController(FallaRepository repositorio) { this.repositorio = repositorio; }

    @GetMapping
    public List<Falla> listar() { return repositorio.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Falla crear(@Valid @RequestBody Falla solicitud, Principal principal) {
        Falla falla = new Falla();
        falla.setComponente(solicitud.getComponente());
        falla.setDetalle(solicitud.getDetalle());
        falla.setPrioridad(solicitud.getPrioridad());
        falla.setReportadoPor(principal.getName());
        falla.setFecha(System.currentTimeMillis());
        return repositorio.save(falla);
    }

    public record EstadoRequest(boolean resuelta) {}

    @PutMapping("/{id}/resuelta")
    public Falla cambiarEstado(@PathVariable Long id, @RequestBody EstadoRequest solicitud) {
        Falla falla = repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Falla no encontrada"));
        falla.setResuelta(solicitud.resuelta());
        return repositorio.save(falla);
    }
}
