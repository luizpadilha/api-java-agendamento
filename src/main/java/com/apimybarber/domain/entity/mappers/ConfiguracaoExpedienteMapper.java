package com.apimybarber.domain.entity.mappers;

import com.apimybarber.domain.entity.Configuracao;
import com.apimybarber.domain.entity.ConfiguracaoExpediente;
import com.apimybarber.domain.services.interfaces.IConfiguracaoService;
import com.apimybarber.domain.viewobject.ConfiguracaoExpedienteVO;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class ConfiguracaoExpedienteMapper implements EntityMapper<ConfiguracaoExpedienteVO, ConfiguracaoExpediente> {

    private final IConfiguracaoService configuracaoService;

    public ConfiguracaoExpedienteMapper(IConfiguracaoService configuracaoService) {
        this.configuracaoService = configuracaoService;
    }

    @Override
    public ConfiguracaoExpediente toEntity(ConfiguracaoExpedienteVO vo, ConfiguracaoExpediente entity) {
        LocalTime inicioExpediente = LocalTime.parse(vo.inicioExpediente());
        LocalTime finalExpediente = LocalTime.parse(vo.finalExpediente());
        LocalTime inicioAlmoco = LocalTime.parse(vo.inicioAlmoco());
        LocalTime finalAlmoco = LocalTime.parse(vo.finalAlmoco());
        if (entity == null) {
            Configuracao configuracao = configuracaoService.buscar(vo.idConfig());
            entity = new ConfiguracaoExpediente(vo.id(), inicioExpediente, finalExpediente, inicioAlmoco, finalAlmoco, configuracao, vo.diaSemana(), null);
        } else {
            entity.setInicioExpediente(inicioExpediente);
            entity.setFinalExpediente(finalExpediente);
            entity.setInicioAlmoco(inicioAlmoco);
            entity.setFinalAlmoco(finalAlmoco);
            entity.setDiaSemana(vo.diaSemana());
        }
        return entity;
    }
}
