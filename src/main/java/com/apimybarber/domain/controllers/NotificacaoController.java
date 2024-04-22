package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Notificacao;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.services.NotificacaoService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.viewobject.NotificacaoVO;
import com.apimybarber.domain.viewobject.RegisterVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/notificacao")
@CrossOrigin
public class NotificacaoController {

    private Logger logger = LoggerFactory.getLogger(NotificacaoController.class);

    @Autowired
    private NotificacaoService service;
    @Autowired
    private UserService userService;


    @GetMapping(value = "/notificacoes")
    public ResponseEntity<List<Notificacao>> notificacoes(@RequestParam String userId) {
        try {
            List<Notificacao> notificacoes = service.findAllByUser_Id(userId);
            return ResponseEntity.ok(notificacoes);
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PostMapping(value = "/salvar-notificacoes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> salvarNotificacoes(@RequestBody NotificacaoVO data) {
        try {
            User user = userService.buscar(data.userId());
            if (user == null) return ResponseEntity.badRequest().build();
            Notificacao notificacao = new Notificacao(null, data.titulo(), data.descricao(), user);
            service.gravar(notificacao);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
