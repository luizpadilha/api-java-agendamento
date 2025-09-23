package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Configuracao;
import com.apimybarber.domain.entity.ConfiguracaoExpediente;
import com.apimybarber.domain.entity.mappers.ConfiguracaoExpedienteMapper;
import com.apimybarber.domain.services.interfaces.IConfiguracaoExpedienteService;
import com.apimybarber.domain.services.interfaces.IConfiguracaoService;
import com.apimybarber.domain.viewobject.ConfiguracaoExpedienteVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/configuracao-expediente")
@CrossOrigin
public class ConfiguracaoExpedienteController {

    private Logger logger = LoggerFactory.getLogger(ConfiguracaoExpedienteController.class);

    private final IConfiguracaoExpedienteService configuracaoExpedienteService;
    private final IConfiguracaoService configuracaoService;
    private final ConfiguracaoExpedienteMapper configuracaoExpedienteMapper;

    public ConfiguracaoExpedienteController(IConfiguracaoExpedienteService configuracaoExpedienteService, IConfiguracaoService configuracaoService, ConfiguracaoExpedienteMapper configuracaoExpedienteMapper) {
        this.configuracaoExpedienteService = configuracaoExpedienteService;
        this.configuracaoService = configuracaoService;
        this.configuracaoExpedienteMapper = configuracaoExpedienteMapper;
    }

    @GetMapping(value = "/configs-expediente")
    public ResponseEntity<List<ConfiguracaoExpediente>> configsExpedientes(@RequestParam String configId) {
        try {
            Configuracao configuracao = configuracaoService.buscar(configId);
            List<ConfiguracaoExpediente> configs = configuracao.getExpedientes();
            configs.forEach(obj -> obj.setIdConfig(configId));
            Collections.sort(configs);
            return ResponseEntity.ok(configs);
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/salvar-configs-expediente", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> salvarConfigExpediente(@RequestBody ConfiguracaoExpedienteVO data) {
        try {
            ConfiguracaoExpediente configuracaoExpediente = configuracaoExpedienteService.buscar(data.id());
            configuracaoExpediente = configuracaoExpedienteMapper.toEntity(data, configuracaoExpediente);
            configuracaoExpedienteService.gravar(configuracaoExpediente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/remover-configs-expediente")
    public ResponseEntity<String> removerConfigExpediente(@RequestParam String configExpedId) {
        try {
            configuracaoExpedienteService.excluir(configExpedId);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pessoa vinculada a outro cadastro.");
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
