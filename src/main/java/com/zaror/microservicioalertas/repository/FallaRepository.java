package com.zaror.microservicioalertas.repository;

import com.zaror.microservicioalertas.model.Falla;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FallaRepository extends JpaRepository<Falla, Long> {}
