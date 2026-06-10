package com.sergio.bankcore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OperacionRequest(
        @NotNull @DecimalMin(value = "0.01") BigDecimal cantidad,
        @Size(max = 160) String descripcion
) {}
