package com.sergio.bankcore.service;

import com.sergio.bankcore.dto.TransferenciaRequest;
import com.sergio.bankcore.dto.TransferenciaResponse;
import com.sergio.bankcore.exception.BadRequestException;
import com.sergio.bankcore.model.*;
import com.sergio.bankcore.repository.AlertaFraudeRepository;
import com.sergio.bankcore.repository.TransferenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferenciaService {
    private static final BigDecimal LIMITE_REVISION = new BigDecimal("3000.00");

    private final TransferenciaRepository transferenciaRepository;
    private final AlertaFraudeRepository alertaFraudeRepository;
    private final CuentaService cuentaService;
    private final MovimientoService movimientoService;

    public TransferenciaService(TransferenciaRepository transferenciaRepository,
                                AlertaFraudeRepository alertaFraudeRepository,
                                CuentaService cuentaService,
                                MovimientoService movimientoService) {
        this.transferenciaRepository = transferenciaRepository;
        this.alertaFraudeRepository = alertaFraudeRepository;
        this.cuentaService = cuentaService;
        this.movimientoService = movimientoService;
    }

    public List<TransferenciaResponse> listar() {
        return transferenciaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public TransferenciaResponse realizar(TransferenciaRequest request) {
        if (request.cuentaOrigenId().equals(request.cuentaDestinoId())) {
            throw new BadRequestException("La cuenta origen y destino no pueden ser la misma");
        }

        Cuenta origen = cuentaService.findEntity(request.cuentaOrigenId());
        Cuenta destino = cuentaService.findEntity(request.cuentaDestinoId());
        Transferencia transferencia = new Transferencia(origen, destino, request.cantidad(), request.concepto());

        String motivoRechazo = validarOperacion(origen, destino, request.cantidad());
        if (motivoRechazo != null) {
            transferencia.setEstado(EstadoTransferencia.RECHAZADA);
            transferencia.setMotivoRechazo(motivoRechazo);
            return toResponse(transferenciaRepository.save(transferencia));
        }

        String motivoRevision = evaluarRiesgo(origen, request.cantidad());
        if (motivoRevision != null) {
            transferencia.setEstado(EstadoTransferencia.PENDIENTE_REVISION);
            transferencia.setMotivoRechazo(motivoRevision);
            transferencia = transferenciaRepository.save(transferencia);
            alertaFraudeRepository.save(new AlertaFraude(origen, transferencia, motivoRevision));
            return toResponse(transferencia);
        }

        origen.setSaldo(origen.getSaldo().subtract(request.cantidad()));
        destino.setSaldo(destino.getSaldo().add(request.cantidad()));

        transferencia.setEstado(EstadoTransferencia.COMPLETADA);
        transferencia = transferenciaRepository.save(transferencia);

        movimientoService.registrar(origen, TipoMovimiento.TRANSFERENCIA_ENVIADA, request.cantidad(), "Transferencia enviada: " + request.concepto());
        movimientoService.registrar(destino, TipoMovimiento.TRANSFERENCIA_RECIBIDA, request.cantidad(), "Transferencia recibida: " + request.concepto());

        return toResponse(transferencia);
    }

    private String validarOperacion(Cuenta origen, Cuenta destino, BigDecimal cantidad) {
        if (origen.getEstado() != EstadoCuenta.ACTIVA) {
            return "La cuenta origen no está activa";
        }
        if (destino.getEstado() == EstadoCuenta.CERRADA) {
            return "La cuenta destino está cerrada";
        }
        if (origen.getSaldo().compareTo(cantidad) < 0) {
            return "Saldo insuficiente";
        }
        return null;
    }

    private String evaluarRiesgo(Cuenta origen, BigDecimal cantidad) {
        if (cantidad.compareTo(LIMITE_REVISION) > 0) {
            return "Operación superior a 3.000 €, requiere revisión antifraude";
        }
        LocalDateTime haceDiezMinutos = LocalDateTime.now().minusMinutes(10);
        long transferenciasRecientes = transferenciaRepository.countByCuentaOrigenIdAndFechaAfter(origen.getId(), haceDiezMinutos);
        if (transferenciasRecientes >= 3) {
            return "Más de 3 transferencias en menos de 10 minutos";
        }
        return null;
    }

    private TransferenciaResponse toResponse(Transferencia transferencia) {
        return new TransferenciaResponse(
                transferencia.getId(),
                transferencia.getCuentaOrigen().getId(),
                transferencia.getCuentaDestino().getId(),
                transferencia.getCantidad(),
                transferencia.getConcepto(),
                transferencia.getFecha(),
                transferencia.getEstado(),
                transferencia.getMotivoRechazo()
        );
    }
}
