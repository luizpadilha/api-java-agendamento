package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Configuracao;
import com.apimybarber.domain.services.interfaces.IConfiguracaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracao")
@CrossOrigin
public class ConfiguracaoController {

    private Logger logger = LoggerFactory.getLogger(ConfiguracaoController.class);

    private final IConfiguracaoService configuracaoService;

    public ConfiguracaoController(IConfiguracaoService configuracaoService) {
        this.configuracaoService = configuracaoService;
    }


    @GetMapping(value = "/config")
    public ResponseEntity<Configuracao> config(@RequestParam String userId) {
        try {
            Configuracao configuracao = configuracaoService.findAllByUser_Id(userId).stream().findFirst().orElse(null);
            if (configuracao == null) {
                configuracao = configuracaoService.criarConfiguracaoPadrao(userId);
            }
            return ResponseEntity.ok(configuracao);
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
