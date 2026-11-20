package com.zaror.microservicioalertas.repository;

import com.zaror.microservicioalertas.model.Comunicado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {}
