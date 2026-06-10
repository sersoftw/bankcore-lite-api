package com.sergio.bankcore.repository;

import com.sergio.bankcore.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByIban(String iban);
    List<Cuenta> findByClienteId(Long clienteId);
}
