package com.sergio.bankcore.dto;

import com.sergio.bankcore.model.EstadoCuenta;
import com.sergio.bankcore.model.TipoCuenta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CuentaResponse(
        Long id,
        String iban,
        Long clienteId,
        String titular,
        BigDecimal saldo,
        TipoCuenta tipoCuenta,
        EstadoCuenta estado,
        LocalDateTime fechaCreacion
) {}
