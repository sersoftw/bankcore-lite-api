package com.sergio.bankcore.controller;

import com.sergio.bankcore.dto.AlertaFraudeResponse;
import com.sergio.bankcore.service.AlertaFraudeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas-fraude")
public class AlertaFraudeController {
    private final AlertaFraudeService alertaFraudeService;

    public AlertaFraudeController(AlertaFraudeService alertaFraudeService) {
        this.alertaFraudeService = alertaFraudeService;
    }

    @GetMapping
    public List<AlertaFraudeResponse> listar() {
        return alertaFraudeService.listar();
    }

    @PutMapping("/{id}/revisar")
    public AlertaFraudeResponse revisar(@PathVariable Long id) {
        return alertaFraudeService.revisar(id);
    }
}
