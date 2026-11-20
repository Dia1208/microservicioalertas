package com.zaror.microservicioalertas.service;

import com.zaror.microservicioalertas.model.Alerta;
import com.zaror.microservicioalertas.repository.AlertaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;

    public AlertaService(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    public List<Alerta> listarAlertas() {
        return alertaRepository.findAll();
    }

    public Alerta buscarAlerta(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Alerta no encontrada"
                ));
    }

    public Alerta guardarAlerta(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    public Alerta actualizarAlerta(Long id, Alerta datos) {
        Alerta alerta = buscarAlerta(id);
        alerta.setTitulo(datos.getTitulo());
        alerta.setDescripcion(datos.getDescripcion());
        alerta.setTipo(datos.getTipo());
        alerta.setUbicacion(datos.getUbicacion());
        alerta.setEstado(datos.getEstado());
        // La actualización de estado conserva la evidencia y los datos originales.
        return alertaRepository.save(alerta);
    }

    public void eliminarAlerta(Long id) {
        if (!alertaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Alerta no encontrada"
            );
        }
        alertaRepository.deleteById(id);
    }
}
