package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Configuracao;
import com.apimybarber.domain.entity.ConfiguracaoExpediente;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.services.ConfiguracaoService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.viewobject.ConfiguracaoExpedienteVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/configuracao")
@CrossOrigin
public class ConfiguracaoController {

    private Logger logger = LoggerFactory.getLogger(ConfiguracaoController.class);

    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private UserService userService;


    @GetMapping(value = "/config")
    public ResponseEntity<Configuracao> config(@RequestParam String userId) {
        try {
            Configuracao configuracao = configuracaoService.findAllByUser_Id(userId).stream().findFirst().orElse(null);
            if (configuracao == null) {
                User user = userService.buscar(userId);
                configuracao = configuracaoService.criarConfiguracaoPadrao(user);
            }
            return ResponseEntity.ok(configuracao);
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
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
            ConfiguracaoExpediente configuracaoExpediente = configuracaoService.buscarConfiguracaoExpediente(data.id());
            Configuracao configuracao = configuracaoService.buscar(data.idConfig());
            LocalTime inicioExpediente = LocalTime.parse(data.inicioExpediente());
            LocalTime finalExpediente = LocalTime.parse(data.finalExpediente());
            LocalTime inicioAlmoco = LocalTime.parse(data.inicioAlmoco());
            LocalTime finalAlmoco = LocalTime.parse(data.finalAlmoco());
            if (configuracaoExpediente == null) {
                configuracaoExpediente = new ConfiguracaoExpediente(data.id(), inicioExpediente, finalExpediente, inicioAlmoco, finalAlmoco, configuracao, data.diaSemana(), null);
            } else {
                configuracaoExpediente.setInicioExpediente(inicioExpediente);
                configuracaoExpediente.setFinalExpediente(finalExpediente);
                configuracaoExpediente.setInicioAlmoco(inicioAlmoco);
                configuracaoExpediente.setFinalAlmoco(finalAlmoco);
                configuracaoExpediente.setDiaSemana(data.diaSemana());
            }
            configuracaoService.gravarConfiguracaoExpediente(configuracaoExpediente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/remover-configs-expediente")
    public ResponseEntity<String> removerConfigExpediente(@RequestParam String configExpedId) {
        try {
            configuracaoService.excluirConfiguracaoExpediente(configExpedId);
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
