package com.sergio.bankcore.controller;

import com.sergio.bankcore.dto.MovimientoResponse;
import com.sergio.bankcore.dto.OperacionRequest;
import com.sergio.bankcore.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas/{cuentaId}")
public class MovimientoController {
    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping("/movimientos")
    public List<MovimientoResponse> listarPorCuenta(@PathVariable Long cuentaId) {
        return movimientoService.listarPorCuenta(cuentaId);
    }

    @PostMapping("/ingresos")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponse ingresar(@PathVariable Long cuentaId, @Valid @RequestBody OperacionRequest request) {
        return movimientoService.ingresar(cuentaId, request);
    }

    @PostMapping("/reintegros")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponse reintegrar(@PathVariable Long cuentaId, @Valid @RequestBody OperacionRequest request) {
        return movimientoService.reintegrar(cuentaId, request);
    }
}
