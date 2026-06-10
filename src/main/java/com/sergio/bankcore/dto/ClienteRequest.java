package com.sergio.bankcore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank @Size(max = 80) String nombre,
        @NotBlank @Size(max = 120) String apellidos,
        @NotBlank @Size(max = 20) String dni,
        @NotBlank @Email @Size(max = 120) String email,
        @Size(max = 20) String telefono
) {}
