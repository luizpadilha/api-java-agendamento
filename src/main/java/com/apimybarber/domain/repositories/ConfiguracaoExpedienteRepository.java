package com.apimybarber.domain.repositories;

import com.apimybarber.domain.entity.ConfiguracaoExpediente;
import com.apimybarber.domain.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfiguracaoExpedienteRepository extends JpaRepository<ConfiguracaoExpediente, String> {

    List<ConfiguracaoExpediente> findAllByConfiguracao_IdAndDiaSemana(String configuracao_id, DiaSemana diaSemana);
}
