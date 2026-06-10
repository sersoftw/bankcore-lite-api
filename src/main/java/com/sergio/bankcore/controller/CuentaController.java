package com.sergio.bankcore.controller;

import com.sergio.bankcore.dto.CuentaRequest;
import com.sergio.bankcore.dto.CuentaResponse;
import com.sergio.bankcore.dto.SaldoResponse;
import com.sergio.bankcore.service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public List<CuentaResponse> listar() {
        return cuentaService.listar();
    }

    @GetMapping("/{id}")
    public CuentaResponse buscarPorId(@PathVariable Long id) {
        return cuentaService.buscarPorId(id);
    }

    @GetMapping("/{id}/saldo")
    public SaldoResponse consultarSaldo(@PathVariable Long id) {
        return cuentaService.consultarSaldo(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponse crear(@Valid @RequestBody CuentaRequest request) {
        return cuentaService.crear(request);
    }

    @PutMapping("/{id}/bloquear")
    public CuentaResponse bloquear(@PathVariable Long id) {
        return cuentaService.bloquear(id);
    }

    @PutMapping("/{id}/activar")
    public CuentaResponse activar(@PathVariable Long id) {
        return cuentaService.activar(id);
    }
}
