package com.sergio.bankcore.controller;

import com.sergio.bankcore.dto.TransferenciaRequest;
import com.sergio.bankcore.dto.TransferenciaResponse;
import com.sergio.bankcore.service.TransferenciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {
    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @GetMapping
    public List<TransferenciaResponse> listar() {
        return transferenciaService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferenciaResponse realizar(@Valid @RequestBody TransferenciaRequest request) {
        return transferenciaService.realizar(request);
    }
}
