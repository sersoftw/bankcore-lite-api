package com.sergio.bankcore.dto;

import com.sergio.bankcore.model.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoResponse(
        Long id,
        Long cuentaId,
        TipoMovimiento tipo,
        BigDecimal cantidad,
        String descripcion,
        LocalDateTime fecha,
        BigDecimal saldoPosterior
) {}
