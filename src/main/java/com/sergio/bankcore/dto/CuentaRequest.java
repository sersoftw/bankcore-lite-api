package com.sergio.bankcore.dto;

import com.sergio.bankcore.model.TipoCuenta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CuentaRequest(
        @NotBlank String iban,
        @NotNull Long clienteId,
        @NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal saldoInicial,
        @NotNull TipoCuenta tipoCuenta
) {}
