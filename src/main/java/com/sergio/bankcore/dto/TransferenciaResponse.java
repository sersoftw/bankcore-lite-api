package com.sergio.bankcore.dto;

import com.sergio.bankcore.model.EstadoTransferencia;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferenciaResponse(
        Long id,
        Long cuentaOrigenId,
        Long cuentaDestinoId,
        BigDecimal cantidad,
        String concepto,
        LocalDateTime fecha,
        EstadoTransferencia estado,
        String motivoRechazo
) {}
