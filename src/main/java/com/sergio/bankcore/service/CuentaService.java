package com.sergio.bankcore.service;

import com.sergio.bankcore.dto.CuentaRequest;
import com.sergio.bankcore.dto.CuentaResponse;
import com.sergio.bankcore.dto.SaldoResponse;
import com.sergio.bankcore.exception.BadRequestException;
import com.sergio.bankcore.exception.ResourceNotFoundException;
import com.sergio.bankcore.model.Cliente;
import com.sergio.bankcore.model.Cuenta;
import com.sergio.bankcore.model.EstadoCuenta;
import com.sergio.bankcore.repository.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuentaService {
    private final CuentaRepository cuentaRepository;
    private final ClienteService clienteService;

    public CuentaService(CuentaRepository cuentaRepository, ClienteService clienteService) {
        this.cuentaRepository = cuentaRepository;
        this.clienteService = clienteService;
    }

    public List<CuentaResponse> listar() {
        return cuentaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CuentaResponse buscarPorId(Long id) {
        return toResponse(findEntity(id));
    }

    public SaldoResponse consultarSaldo(Long id) {
        Cuenta cuenta = findEntity(id);
        return new SaldoResponse(cuenta.getId(), cuenta.getIban(), cuenta.getSaldo());
    }

    @Transactional
    public CuentaResponse crear(CuentaRequest request) {
        cuentaRepository.findByIban(request.iban()).ifPresent(c -> {
            throw new BadRequestException("Ya existe una cuenta con ese IBAN");
        });
        Cliente cliente = clienteService.findEntity(request.clienteId());
        Cuenta cuenta = new Cuenta(request.iban(), cliente, request.saldoInicial(), request.tipoCuenta());
        return toResponse(cuentaRepository.save(cuenta));
    }

    @Transactional
    public CuentaResponse bloquear(Long id) {
        Cuenta cuenta = findEntity(id);
        cuenta.setEstado(EstadoCuenta.BLOQUEADA);
        return toResponse(cuenta);
    }

    @Transactional
    public CuentaResponse activar(Long id) {
        Cuenta cuenta = findEntity(id);
        cuenta.setEstado(EstadoCuenta.ACTIVA);
        return toResponse(cuenta);
    }

    public Cuenta findEntity(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id " + id));
    }

    public CuentaResponse toResponse(Cuenta cuenta) {
        String titular = cuenta.getCliente().getNombre() + " " + cuenta.getCliente().getApellidos();
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getIban(),
                cuenta.getCliente().getId(),
                titular,
                cuenta.getSaldo(),
                cuenta.getTipoCuenta(),
                cuenta.getEstado(),
                cuenta.getFechaCreacion()
        );
    }
}
