package com.apimybarber.domain.repositories;

import com.apimybarber.domain.entity.ConfiguracaoExpediente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracaoExpedienteRepository extends JpaRepository<ConfiguracaoExpediente, String> {
}
