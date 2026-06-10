package com.sergio.bankcore.service;

import com.sergio.bankcore.dto.AlertaFraudeResponse;
import com.sergio.bankcore.exception.ResourceNotFoundException;
import com.sergio.bankcore.model.AlertaFraude;
import com.sergio.bankcore.model.EstadoAlerta;
import com.sergio.bankcore.repository.AlertaFraudeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlertaFraudeService {
    private final AlertaFraudeRepository alertaFraudeRepository;

    public AlertaFraudeService(AlertaFraudeRepository alertaFraudeRepository) {
        this.alertaFraudeRepository = alertaFraudeRepository;
    }

    public List<AlertaFraudeResponse> listar() {
        return alertaFraudeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public AlertaFraudeResponse revisar(Long id) {
        AlertaFraude alerta = alertaFraudeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta no encontrada con id " + id));
        alerta.setEstado(EstadoAlerta.REVISADA);
        return toResponse(alerta);
    }

    private AlertaFraudeResponse toResponse(AlertaFraude alerta) {
        return new AlertaFraudeResponse(
                alerta.getId(),
                alerta.getCuenta().getId(),
                alerta.getTransferencia().getId(),
                alerta.getMotivo(),
                alerta.getFecha(),
                alerta.getEstado()
        );
    }
}
