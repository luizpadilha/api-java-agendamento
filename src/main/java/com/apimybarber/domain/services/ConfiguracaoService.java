package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.Configuracao;
import com.apimybarber.domain.entity.ConfiguracaoExpediente;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.enums.DiaSemana;
import com.apimybarber.domain.repositories.ConfiguracaoRepository;
import com.apimybarber.domain.services.interfaces.IConfiguracaoService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ConfiguracaoService extends AbstractService<Configuracao> implements IConfiguracaoService {

    private final ConfiguracaoRepository configuracaoRepository;
    private final UserService userService;


    public ConfiguracaoService(ConfiguracaoRepository configuracaoRepository, UserService userService) {
        this.configuracaoRepository = configuracaoRepository;
        this.userService = userService;
    }

    @Override
    public Configuracao gravar(Configuracao registro) {
        return configuracaoRepository.save(registro);
    }

    @Override
    public Configuracao buscar(String id) {
        return inicializarListas(configuracaoRepository.findById(id).orElse(null));
    }

    @Override
    public void excluir(String id) {
        configuracaoRepository.deleteById(id);
    }

    @Override
    public List<Configuracao> findAllByUser_Id(String id_user) {
        List<Configuracao> configs = configuracaoRepository.findAllByUser_Id(id_user);
        configs.forEach(this::inicializarListas);
        return configs;
    }

    @Override
    public Configuracao inicializarListas(Configuracao configuracao) {
        if (configuracao == null) return null;
        Hibernate.initialize(configuracao.getExpedientes());
        return configuracao;
    }

    @Override
    public Configuracao criarConfiguracaoPadrao(String userId) {
        User user = userService.buscar(userId);
        Configuracao configuracao = new Configuracao(user);
        for (DiaSemana diaSemana : DiaSemana.values()) {
            LocalTime inicioExpediente = LocalTime.parse("08:00");
            LocalTime inicioAlmoco = LocalTime.parse("12:00");
            LocalTime finalAlmoco = LocalTime.parse("13:00");
            LocalTime finalExpediente = LocalTime.parse("19:00");
            ConfiguracaoExpediente configuracaoExpediente = new ConfiguracaoExpediente(null, inicioExpediente, finalExpediente, inicioAlmoco, finalAlmoco, configuracao, diaSemana, null);
            configuracao.getExpedientes().add(configuracaoExpediente);
        }
        return gravar(configuracao);
    }
}
