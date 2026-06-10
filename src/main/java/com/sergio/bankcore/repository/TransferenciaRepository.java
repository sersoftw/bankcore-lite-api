package com.sergio.bankcore.repository;

import com.sergio.bankcore.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    long countByCuentaOrigenIdAndFechaAfter(Long cuentaOrigenId, LocalDateTime fecha);
}
