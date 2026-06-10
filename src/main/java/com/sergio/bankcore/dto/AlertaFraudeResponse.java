package com.sergio.bankcore.dto;

import com.sergio.bankcore.model.EstadoAlerta;

import java.time.LocalDateTime;

public record AlertaFraudeResponse(
        Long id,
        Long cuentaId,
        Long transferenciaId,
        String motivo,
        LocalDateTime fecha,
        EstadoAlerta estado
) {}
