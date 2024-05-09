package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.ConfiguracaoExpediente;
import com.apimybarber.domain.repositories.ConfiguracaoExpedienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracaoExpedienteService extends AbstractService<ConfiguracaoExpediente> {

    @Autowired
    private ConfiguracaoExpedienteRepository configuracaoExpedienteRepository;

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
}
