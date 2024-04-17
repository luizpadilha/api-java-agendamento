package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.entity.Pessoa;
import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.services.AgendaService;
import com.apimybarber.domain.services.PessoaService;
import com.apimybarber.domain.services.ServicoService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.utils.LocalDateUtils;
import com.apimybarber.domain.viewobject.AgendaVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agenda")
@CrossOrigin
public class AgendaController {

    private Logger logger = LoggerFactory.getLogger(AgendaController.class);

    @Autowired
    private AgendaService service;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private UserService userService;


    @GetMapping(value = "/agendas")
    public ResponseEntity<List<Agenda>> agendas(@RequestParam String userId, @RequestParam String data) {
        try {
            LocalDate dataFiltro = LocalDateUtils.getLocalDateIso(data);
            List<Agenda> agendas = service.findAllByUserIdAndHorario(userId, dataFiltro);
            return ResponseEntity.ok(agendas);
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(401).build();
        }
    }


    @PostMapping(value = "/salvar-agenda", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> salvarAgenda(@RequestBody AgendaVO data) {
        try {
            User user = userService.buscar(data.userId());
            if (user == null) return ResponseEntity.status(402).build();
            Agenda agenda = service.buscar(data.id());
            Pessoa pessoa = pessoaService.buscar(data.pessoa().id());
            Servico servico = servicoService.buscar(data.servico().id());
            if (pessoa == null) {
                pessoa = new Pessoa(data.id(), data.pessoa().nome(), data.pessoa().numero(), user);
            }
            if (servico == null) {
                servico = new Servico(data.id(), data.servico().descricao(), data.servico().preco(), user);
            }
            LocalDateTime horario = LocalDateUtils.getLocalDateTimeIso(data.horarioToIso8601());
            if (agenda == null) {
                agenda = new Agenda(data.id(), pessoa, servico, user, horario);
            } else {
                agenda.setPessoa(pessoa);
                agenda.setServico(servico);
                agenda.setHorario(horario);
            }
            service.gravar(agenda);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping(value = "/remover-agenda")
    public ResponseEntity<Void> removerAgenda(@RequestParam String agendaId) {
        try {
            service.excluir(agendaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(401).build();
        }
    }
}
