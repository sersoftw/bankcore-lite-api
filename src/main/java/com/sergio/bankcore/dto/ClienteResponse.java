package com.sergio.bankcore.dto;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nombre,
        String apellidos,
        String dni,
        String email,
        String telefono,
        LocalDateTime fechaAlta,
        boolean activo
) {}
