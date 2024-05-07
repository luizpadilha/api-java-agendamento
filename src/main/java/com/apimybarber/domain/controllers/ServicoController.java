package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.services.ServicoService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.viewobject.ServicoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
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
            servicos.forEach(serv -> {
                if (serv.getTempo() != null) {
                    serv.setTempoHora(serv.getTempo().getHour());
                    serv.setTempoMinuto(serv.getTempo().getMinute());
                }
            });
            return ResponseEntity.ok(servicos);
        } catch (Exception ex) {
            logger.error("Erro: ", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PostMapping(value = "/salvar-servico", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> salvarServico(@RequestBody ServicoVO data) {
        try {
            User user = userService.buscar(data.userId());
            if (user == null) return ResponseEntity.badRequest().build();
            Servico servico = service.buscar(data.id());
            LocalTime tempo = LocalTime.parse(data.tempo());
            if (servico == null) {
                servico = new Servico(data.id(), data.descricao(), data.preco(), user, tempo);
            } else {
                servico.setDescricao(data.descricao());
                servico.setPreco(data.preco());
                servico.setTempo(tempo);
            }
            service.gravar(servico);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/remover-servico")
    public ResponseEntity<String> removerServico(@RequestParam String servicoId) {
        try {
            service.excluir(servicoId);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Servico vinculada a outro cadastro.");
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
