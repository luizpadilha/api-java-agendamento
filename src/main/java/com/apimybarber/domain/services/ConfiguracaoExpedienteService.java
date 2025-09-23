package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.ConfiguracaoExpediente;
import com.apimybarber.domain.enums.DiaSemana;
import com.apimybarber.domain.services.interfaces.IConfiguracaoExpedienteService;
import com.apimybarber.domain.repositories.ConfiguracaoExpedienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracaoExpedienteService extends AbstractService<ConfiguracaoExpediente> implements IConfiguracaoExpedienteService {

    private final ConfiguracaoExpedienteRepository configuracaoExpedienteRepository;

    public ConfiguracaoExpedienteService(ConfiguracaoExpedienteRepository configuracaoExpedienteRepository) {
        this.configuracaoExpedienteRepository = configuracaoExpedienteRepository;
    }

    @Override
    public ConfiguracaoExpediente gravar(ConfiguracaoExpediente registro) {
        return configuracaoExpedienteRepository.save(registro);
    }

    @Override
    public ConfiguracaoExpediente buscar(String id) {
        return configuracaoExpedienteRepository.findById(id).orElse(null);
    }

    @Override
    public void excluir(String id) {
        configuracaoExpedienteRepository.deleteById(id);
    }

    @Override
    public ConfiguracaoExpediente buscarConfiguracaoExpedientePorConfiguracaoEDiaSemana(String configuracao_id, DiaSemana diaSemana) {
        return configuracaoExpedienteRepository.findAllByConfiguracao_IdAndDiaSemana(configuracao_id, diaSemana).stream().findFirst().orElse(null);
    }
}
