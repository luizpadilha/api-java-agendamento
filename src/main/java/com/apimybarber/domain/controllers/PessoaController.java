package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Pessoa;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.services.PessoaService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.viewobject.PessoaVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoa")
@CrossOrigin
public class PessoaController {

    private Logger logger = LoggerFactory.getLogger(PessoaController.class);

    @Autowired
    private PessoaService service;
    @Autowired
    private UserService userService;


    @GetMapping(value = "/pessoas")
    public ResponseEntity<List<Pessoa>> pessoas(@RequestParam String userId) {
        try {
            List<Pessoa> pessoas = service.findAllByUser_Id(userId);
            return ResponseEntity.ok(pessoas);
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/pessoa")
    public ResponseEntity<Pessoa> pessoa(@RequestParam String pessoaId) {
        try {
            Pessoa pessoa = service.buscar(pessoaId);
            return ResponseEntity.ok(pessoa);
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PostMapping(value = "/salvar-pessoa", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> salvarPessoa(@RequestBody PessoaVO data) {
        try {
            User user = userService.buscar(data.userId());
            if (user == null) return ResponseEntity.badRequest().build();
            Pessoa pessoa = service.buscar(data.id());
            if (pessoa == null) {
                pessoa = new Pessoa(data.id(), data.nome(), data.numero(), user);
            } else {
                pessoa.setNome(data.nome());
                pessoa.setNumero(data.numero());
            }
            pessoa = service.gravar(pessoa);
            return ResponseEntity.ok(pessoa.getId());
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/remover-pessoa")
    public ResponseEntity<String> removerPessoa(@RequestParam String pessoaId) {
        try {
            service.excluir(pessoaId);
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
