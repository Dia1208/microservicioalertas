package com.zaror.microservicioalertas.service;

import com.zaror.microservicioalertas.model.Alerta;
import com.zaror.microservicioalertas.repository.AlertaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertaServiceTest {

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private AlertaService alertaService;

    @Test
    void listaTodasLasAlertas() {
        List<Alerta> alertas = List.of(alerta("Incendio"), alerta("Robo"));
        when(alertaRepository.findAll()).thenReturn(alertas);

        assertEquals(alertas, alertaService.listarAlertas());
        verify(alertaRepository).findAll();
    }

    @Test
    void buscaUnaAlertaExistentePorId() {
        Alerta alerta = alerta("Incendio");
        when(alertaRepository.findById(7L)).thenReturn(Optional.of(alerta));

        assertEquals(alerta, alertaService.buscarAlerta(7L));
    }

    @Test
    void devuelve404CuandoLaAlertaBuscadaNoExiste() {
        when(alertaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> alertaService.buscarAlerta(99L)
        );
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void guardaUnaAlertaNueva() {
        Alerta alerta = alerta("Incendio");
        when(alertaRepository.save(alerta)).thenReturn(alerta);

        assertEquals(alerta, alertaService.guardarAlerta(alerta));
        verify(alertaRepository).save(alerta);
    }

    @Test
    void actualizaLosCamposDeUnaAlertaExistente() {
        Alerta existente = alerta("Incendio");
        Alerta datos = alerta("Robo");
        when(alertaRepository.findById(7L)).thenReturn(Optional.of(existente));
        when(alertaRepository.save(existente)).thenReturn(existente);

        Alerta resultado = alertaService.actualizarAlerta(7L, datos);

        assertEquals("Robo", resultado.getTipo());
        assertEquals("Robo", resultado.getTitulo());
        assertEquals("Descripción de prueba", resultado.getDescripcion());
        assertEquals("Cerrillos", resultado.getUbicacion());
        assertEquals("Activa", resultado.getEstado());
        verify(alertaRepository).save(existente);
    }

    @Test
    void devuelve404YNoGuardaSiLaAlertaActualizadaNoExiste() {
        when(alertaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> alertaService.actualizarAlerta(99L, alerta("Robo"))
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    void eliminaUnaAlertaExistente() {
        when(alertaRepository.existsById(7L)).thenReturn(true);

        alertaService.eliminarAlerta(7L);

        verify(alertaRepository).deleteById(7L);
    }

    @Test
    void devuelve404YNoEliminaSiLaAlertaNoExiste() {
        when(alertaRepository.existsById(99L)).thenReturn(false);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> alertaService.eliminarAlerta(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(alertaRepository, never()).deleteById(99L);
    }

    private Alerta alerta(String tipo) {
        Alerta alerta = new Alerta();
        alerta.setTitulo(tipo);
        alerta.setDescripcion("Descripción de prueba");
        alerta.setTipo(tipo);
        alerta.setUbicacion("Cerrillos");
        alerta.setEstado("Activa");
        return alerta;
    }
}
