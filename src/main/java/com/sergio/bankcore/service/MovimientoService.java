package com.sergio.bankcore.service;

import com.sergio.bankcore.dto.MovimientoResponse;
import com.sergio.bankcore.dto.OperacionRequest;
import com.sergio.bankcore.exception.BadRequestException;
import com.sergio.bankcore.model.Cuenta;
import com.sergio.bankcore.model.EstadoCuenta;
import com.sergio.bankcore.model.Movimiento;
import com.sergio.bankcore.model.TipoMovimiento;
import com.sergio.bankcore.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final CuentaService cuentaService;

    public MovimientoService(MovimientoRepository movimientoRepository, CuentaService cuentaService) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaService = cuentaService;
    }

    public List<MovimientoResponse> listarPorCuenta(Long cuentaId) {
        cuentaService.findEntity(cuentaId);
        return movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public MovimientoResponse ingresar(Long cuentaId, OperacionRequest request) {
        Cuenta cuenta = cuentaService.findEntity(cuentaId);
        validarCuentaOperativa(cuenta);
        cuenta.setSaldo(cuenta.getSaldo().add(request.cantidad()));
        Movimiento movimiento = registrar(cuenta, TipoMovimiento.INGRESO, request.cantidad(), descripcion(request.descripcion(), "Ingreso"));
        return toResponse(movimiento);
    }

    @Transactional
    public MovimientoResponse reintegrar(Long cuentaId, OperacionRequest request) {
        Cuenta cuenta = cuentaService.findEntity(cuentaId);
        validarCuentaOperativa(cuenta);
        if (cuenta.getSaldo().compareTo(request.cantidad()) < 0) {
            throw new BadRequestException("Saldo insuficiente para realizar el reintegro");
        }
        cuenta.setSaldo(cuenta.getSaldo().subtract(request.cantidad()));
        Movimiento movimiento = registrar(cuenta, TipoMovimiento.REINTEGRO, request.cantidad(), descripcion(request.descripcion(), "Reintegro"));
        return toResponse(movimiento);
    }

    @Transactional
    public Movimiento registrar(Cuenta cuenta, TipoMovimiento tipo, BigDecimal cantidad, String descripcion) {
        Movimiento movimiento = new Movimiento(cuenta, tipo, cantidad, descripcion, cuenta.getSaldo());
        return movimientoRepository.save(movimiento);
    }

    private void validarCuentaOperativa(Cuenta cuenta) {
        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            throw new BadRequestException("La cuenta no está activa");
        }
    }

    private String descripcion(String descripcion, String defecto) {
        return descripcion == null || descripcion.isBlank() ? defecto : descripcion;
    }

    private MovimientoResponse toResponse(Movimiento movimiento) {
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getCuenta().getId(),
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getDescripcion(),
                movimiento.getFecha(),
                movimiento.getSaldoPosterior()
        );
    }
}
