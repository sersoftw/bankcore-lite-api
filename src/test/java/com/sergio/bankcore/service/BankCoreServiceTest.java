package com.sergio.bankcore.service;

import com.sergio.bankcore.dto.*;
import com.sergio.bankcore.model.EstadoTransferencia;
import com.sergio.bankcore.model.TipoCuenta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BankCoreServiceTest {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private TransferenciaService transferenciaService;

    @Test
    void ingresoAumentaElSaldoCorrectamente() {
        CuentaResponse cuenta = crearCuentaConSaldo("100.00");

        movimientoService.ingresar(cuenta.id(), new OperacionRequest(new BigDecimal("50.00"), "Ingreso test"));

        SaldoResponse saldo = cuentaService.consultarSaldo(cuenta.id());
        assertThat(saldo.saldo()).isEqualByComparingTo("150.00");
    }

    @Test
    void transferenciaSeRechazaPorSaldoInsuficiente() {
        CuentaResponse origen = crearCuentaConSaldo("20.00");
        CuentaResponse destino = crearCuentaConSaldo("0.00");

        TransferenciaResponse response = transferenciaService.realizar(new TransferenciaRequest(
                origen.id(), destino.id(), new BigDecimal("100.00"), "Pago imposible"
        ));

        assertThat(response.estado()).isEqualTo(EstadoTransferencia.RECHAZADA);
        assertThat(response.motivoRechazo()).containsIgnoringCase("saldo insuficiente");
    }

    @Test
    void transferenciaSuperiorATresMilQuedaPendienteDeRevision() {
        CuentaResponse origen = crearCuentaConSaldo("10000.00");
        CuentaResponse destino = crearCuentaConSaldo("0.00");

        TransferenciaResponse response = transferenciaService.realizar(new TransferenciaRequest(
                origen.id(), destino.id(), new BigDecimal("3500.00"), "Operación elevada"
        ));

        assertThat(response.estado()).isEqualTo(EstadoTransferencia.PENDIENTE_REVISION);
        assertThat(response.motivoRechazo()).contains("3.000");
    }

    @Test
    void transferenciaNormalSeCompletaYActualizaSaldos() {
        CuentaResponse origen = crearCuentaConSaldo("1000.00");
        CuentaResponse destino = crearCuentaConSaldo("100.00");

        TransferenciaResponse response = transferenciaService.realizar(new TransferenciaRequest(
                origen.id(), destino.id(), new BigDecimal("200.00"), "Pago alquiler"
        ));

        assertThat(response.estado()).isEqualTo(EstadoTransferencia.COMPLETADA);
        assertThat(cuentaService.consultarSaldo(origen.id()).saldo()).isEqualByComparingTo("800.00");
        assertThat(cuentaService.consultarSaldo(destino.id()).saldo()).isEqualByComparingTo("300.00");
    }

    @Test
    void transferenciaDesdeCuentaBloqueadaSeRechaza() {
        CuentaResponse origen = crearCuentaConSaldo("1000.00");
        CuentaResponse destino = crearCuentaConSaldo("0.00");
        cuentaService.bloquear(origen.id());

        TransferenciaResponse response = transferenciaService.realizar(new TransferenciaRequest(
                origen.id(), destino.id(), new BigDecimal("100.00"), "Intento bloqueado"
        ));

        assertThat(response.estado()).isEqualTo(EstadoTransferencia.RECHAZADA);
        assertThat(response.motivoRechazo()).containsIgnoringCase("origen no está activa");
    }

    private CuentaResponse crearCuentaConSaldo(String saldo) {
        long now = System.nanoTime();
        ClienteResponse cliente = clienteService.crear(new ClienteRequest(
                "Cliente" + now,
                "Test",
                "DNI" + now,
                "cliente" + now + "@example.com",
                "600000000"
        ));
        return cuentaService.crear(new CuentaRequest(
                "ES" + now,
                cliente.id(),
                new BigDecimal(saldo),
                TipoCuenta.CORRIENTE
        ));
    }
}
