package com.sergio.bankcore.dto;

import java.math.BigDecimal;

public record SaldoResponse(
        Long cuentaId,
        String iban,
        BigDecimal saldo
) {}
