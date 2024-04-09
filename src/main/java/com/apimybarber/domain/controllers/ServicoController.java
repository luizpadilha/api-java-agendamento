package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.services.ServicoService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.viewobject.ServicoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/servico")
@CrossOrigin
public class ServicoController {

    private Logger logger = LoggerFactory.getLogger(ServicoController.class);

    @Autowired
    private ServicoService service;
    @Autowired
    private UserService userService;


    @GetMapping(value = "/servicos")
    public ResponseEntity<List<Servico>> servicos(@RequestParam String userId) {
        try {
            List<Servico> servicos = service.findAllByUser_Id(userId);
            return ResponseEntity.ok(servicos);
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(401).build();
        }
    }


    @PostMapping(value = "/salvar-servico", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> salvarServico(@RequestBody ServicoVO data) {
        try {
            User user = userService.buscar(data.userId());
            if (user == null) return ResponseEntity.status(402).build();
            Servico servico = service.buscar(data.id());
            if (servico == null) {
                servico = new Servico(data.id(), data.descricao(), data.preco(), user);
            } else {
                servico.setDescricao(data.descricao());
                servico.setPreco(data.preco());
            }
            service.gravar(servico);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping(value = "/remover-servico")
    public ResponseEntity<Void> removerServico(@RequestParam String servicoId) {
        try {
            service.excluir(servicoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(401).build();
        }
    }


}