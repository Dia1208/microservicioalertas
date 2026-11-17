
package com.zaror.microservicioalertas.repository;

import com.zaror.microservicioalertas.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

}
