package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.entity.mappers.ServicoMapper;
import com.apimybarber.domain.services.interfaces.IServicoService;
import com.apimybarber.domain.viewobject.ServicoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/servico")
@CrossOrigin
public class ServicoController {

    private Logger logger = LoggerFactory.getLogger(ServicoController.class);

    private final IServicoService service;
    private final ServicoMapper servicoMapper;

    public ServicoController(IServicoService service, ServicoMapper servicoMapper) {
        this.service = service;
        this.servicoMapper = servicoMapper;
    }

    @GetMapping(value = "/servicos")
    public ResponseEntity<List<Servico>> servicos(@RequestParam String userId) {
        try {
            List<Servico> servicos = service.findAllByUser_Id(userId);
            servicos.forEach(serv -> {
                if (serv.getTempo() != null) {
                    serv.setTempoHora(serv.getTempo().getHour());
                    serv.setTempoMinuto(serv.getTempo().getMinute());
                    serv.setImageBase64(serv.getFileImage() == null ? null : Base64.getEncoder().encodeToString(serv.getFileImage()));
                }
            });
            return ResponseEntity.ok(servicos);
        } catch (Exception ex) {
            logger.error("Erro: ", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/servico")
    public ResponseEntity<Servico> servico(@RequestParam String servicoId) {
        try {
            Servico servico = service.buscar(servicoId);
            if (servico.getTempo() != null) {
                servico.setTempoHora(servico.getTempo().getHour());
                servico.setTempoMinuto(servico.getTempo().getMinute());
                servico.setImageBase64(servico.getFileImage() == null ? null : Base64.getEncoder().encodeToString(servico.getFileImage()));
            }
            return ResponseEntity.ok(servico);
        } catch (Exception ex) {
            logger.error("Erro: ", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PostMapping(value = "/salvar-servico", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> salvarServico(@RequestBody ServicoVO data) {
        try {
            Servico servico = service.buscar(data.id());
            servico = servicoMapper.toEntity(data, servico);
            if (servico == null) {
                return ResponseEntity.badRequest().build();
            }
            servico = service.gravar(servico);
            return ResponseEntity.ok(servico.getId());
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
