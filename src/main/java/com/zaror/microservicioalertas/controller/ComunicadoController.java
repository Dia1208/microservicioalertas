package com.zaror.microservicioalertas.controller;

import com.zaror.microservicioalertas.model.Comunicado;
import com.zaror.microservicioalertas.repository.ComunicadoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/comunicados")
public class ComunicadoController {
    private final ComunicadoRepository repositorio;

    public ComunicadoController(ComunicadoRepository repositorio) { this.repositorio = repositorio; }

    @GetMapping
    public List<Comunicado> listar() { return repositorio.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comunicado crear(@Valid @RequestBody Comunicado solicitud, Principal principal) {
        Comunicado comunicado = new Comunicado();
        comunicado.setTitulo(solicitud.getTitulo());
        comunicado.setMensaje(solicitud.getMensaje());
        comunicado.setAutor(principal.getName());
        comunicado.setFecha(System.currentTimeMillis());
        return repositorio.save(comunicado);
    }
}
