package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.services.interfaces.IAgendaService;
import com.apimybarber.domain.services.interfaces.IHorarioService;
import com.apimybarber.domain.services.interfaces.IServicoService;
import com.apimybarber.domain.utils.LocalDateUtils;
import com.apimybarber.domain.viewobject.HorarioVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/horario")
@CrossOrigin
public class HorarioController {

    private Logger logger = LoggerFactory.getLogger(HorarioController.class);

    private final IAgendaService agendaService;
    private final IServicoService servicoService;
    private final IHorarioService horarioService;

    public HorarioController(IAgendaService agendaService, IServicoService servicoService, IHorarioService horarioService) {
        this.agendaService = agendaService;
        this.servicoService = servicoService;
        this.horarioService = horarioService;
    }

    @GetMapping(value = "/horarios-por-data")
    public ResponseEntity<List<HorarioVO>> horariosPorData(@RequestParam String userId,
                                                           @RequestParam String servicoId,
                                                           @RequestParam String data) {
        try {
            LocalDate localDate = LocalDateUtils.getLocalDateIso(data);
            List<Agenda> agendas = agendaService.findAllByUserIdAndHorario(userId, localDate);
            Servico servico = servicoService.buscar(servicoId);
            return ResponseEntity.ok(horarioService.montarHorariosDisponiveis(servico.getTempo(), agendas, userId, localDate));
        } catch (OutOfMemoryError | Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.ok(new ArrayList<>());
        }
    }


}
