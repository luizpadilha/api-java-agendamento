package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.services.interfaces.IAgendaService;
import com.apimybarber.domain.services.interfaces.IUserService;
import com.apimybarber.domain.entity.mappers.AgendaMapper;
import com.apimybarber.domain.utils.LocalDateUtils;
import com.apimybarber.domain.viewobject.AgendaVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agenda")
@CrossOrigin
public class AgendaController {

    private Logger logger = LoggerFactory.getLogger(AgendaController.class);

    private final IAgendaService service;
    private final IUserService userService;
    private final AgendaMapper agendaMapper;

    public AgendaController(IAgendaService service, IUserService userService, AgendaMapper agendaMapper) {
        this.service = service;
        this.userService = userService;
        this.agendaMapper = agendaMapper;
    }


    @GetMapping(value = "/agendas")
    public ResponseEntity<List<Agenda>> agendas(@RequestParam String userId, @RequestParam String data) {
        try {
            LocalDate dataFiltro = LocalDateUtils.getLocalDateIso(data);
            List<Agenda> agendas = service.findAllByUserIdAndHorario(userId, dataFiltro);
            return getAgendasResponse(agendas);
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/agendas-pessoa")
    public ResponseEntity<List<Agenda>> agendasByPessoa(@RequestParam String userId, @RequestParam String pessoaId) {
        try {
            List<Agenda> agendas = service.findAllByUserIdAndPessoa(userId, pessoaId);
            return getAgendasResponse(agendas);
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private ResponseEntity<List<Agenda>> getAgendasResponse(List<Agenda> agendas) {
        agendas.forEach(agend -> {
            Servico serv = agend.getServico();
            if (serv.getTempo() != null) {
                serv.setTempoHora(serv.getTempo().getHour());
                serv.setTempoMinuto(serv.getTempo().getMinute());
            }
        });
        return ResponseEntity.ok(agendas);
    }


    @PostMapping(value = "/salvar-agenda", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> salvarAgenda(@RequestBody AgendaVO data) {
        try {
            User user = userService.buscar(data.userId());
            if (user == null) return ResponseEntity.badRequest().build();
            Agenda agenda = service.buscar(data.id());
            agenda = agendaMapper.toEntity(data, agenda);
            service.gravar(agenda);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/remover-agenda")
    public ResponseEntity<Void> removerAgenda(@RequestParam String agendaId) {
        try {
            service.excluir(agendaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
