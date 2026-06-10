package com.sergio.bankcore.repository;

import com.sergio.bankcore.model.AlertaFraude;
import com.sergio.bankcore.model.EstadoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaFraudeRepository extends JpaRepository<AlertaFraude, Long> {
    List<AlertaFraude> findByEstado(EstadoAlerta estado);
}
