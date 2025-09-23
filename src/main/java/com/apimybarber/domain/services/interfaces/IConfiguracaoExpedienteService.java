package com.apimybarber.domain.services.interfaces;

import com.apimybarber.domain.entity.ConfiguracaoExpediente;
import com.apimybarber.domain.enums.DiaSemana;

public interface IConfiguracaoExpedienteService {

    ConfiguracaoExpediente gravar(ConfiguracaoExpediente registro);

    ConfiguracaoExpediente buscar(String id);

    void excluir(String id);

    ConfiguracaoExpediente buscarConfiguracaoExpedientePorConfiguracaoEDiaSemana(String configuracao_id, DiaSemana diaSemana);
}
