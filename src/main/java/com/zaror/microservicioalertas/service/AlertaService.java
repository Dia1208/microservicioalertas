
package com.zaror.microservicioalertas.service;

import com.zaror.microservicioalertas.model.Alerta;
import com.zaror.microservicioalertas.repository.AlertaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;

    public AlertaService(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    // Listar todas las alertas
    public List<Alerta> listarAlertas() {
        return alertaRepository.findAll();
    }

    // Buscar una alerta por ID
    public Alerta buscarAlerta(Long id) {
        return alertaRepository.findById(id).orElse(null);
    }

    // Guardar una alerta
    public Alerta guardarAlerta(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    // Actualizar una alerta
    public Alerta actualizarAlerta(Long id, Alerta datos) {

        Alerta alerta = buscarAlerta(id);

        if (alerta != null) {
            alerta.setTitulo(datos.getTitulo());
            alerta.setDescripcion(datos.getDescripcion());
            alerta.setTipo(datos.getTipo());
            alerta.setUbicacion(datos.getUbicacion());
            alerta.setEstado(datos.getEstado());

            return alertaRepository.save(alerta);
        }

        return null;
    }

    // Eliminar una alerta
    public void eliminarAlerta(Long id) {
        if (alertaRepository.existsById(id)) {
            alertaRepository.deleteById(id);
        }
    }
}
