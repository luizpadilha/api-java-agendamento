package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.services.AgendaService;
import com.apimybarber.domain.services.ServicoService;
import com.apimybarber.domain.utils.LocalDateUtils;
import com.apimybarber.domain.viewobject.HorarioVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/horario")
@CrossOrigin
public class HorarioController {

    private Logger logger = LoggerFactory.getLogger(HorarioController.class);

    @Autowired
    private AgendaService agendaService;
    @Autowired
    private ServicoService servicoService;


    @GetMapping(value = "/horarios-por-data")
    public ResponseEntity<List<HorarioVO>> horariosPorData(@RequestParam String userId,
                                                           @RequestParam String servicoId,
                                                           @RequestParam String data) {
        try {
            LocalDate localDate = LocalDateUtils.getLocalDateIso(data);
            List<Agenda> agendas = agendaService.findAllByUserIdAndHorario(userId, localDate);
            Servico servico = servicoService.buscar(servicoId);
            return ResponseEntity.ok(montarHorariosDisponiveis(servico.getTempo(), agendas));
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private static List<HorarioVO> montarHorariosDisponiveis(LocalTime tempoServico, List<Agenda> horariosAgendados) {
        List<LocalTime> horariosDisponiveis = new ArrayList<>();
        List<HorarioVO> horarios = new ArrayList<>();

        LocalTime horarioAtual = LocalTime.of(8, 0); // Horário inicial
        LocalTime horarioFinal = LocalTime.of(17, 0); // Horário final

        for (Agenda agenda : horariosAgendados) {
            LocalTime horarioAgendado = agenda.getHorario().toLocalTime();
            while (horarioAtual.plusMinutes(tempoServico.getMinute()).minusMinutes(1).isBefore(horarioAgendado)) {
                horariosDisponiveis.add(horarioAtual);
                horarioAtual = horarioAtual.plusMinutes(tempoServico.getMinute());
            }
            horarioAtual = horarioAgendado.plusMinutes(agenda.getServico().getTempo().getMinute());
        }

        while (horarioAtual.minusMinutes(1).isBefore(horarioFinal)) {
            horariosDisponiveis.add(horarioAtual);
            horarioAtual = horarioAtual.plusMinutes(tempoServico.getMinute());
        }
        horariosDisponiveis.removeIf(horario -> horario.isAfter(horarioFinal));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (LocalTime horarioDisponivel : horariosDisponiveis) {
            horarios.add(new HorarioVO(horarioDisponivel.format(formatter), true));
        }
         return horarios;
    }
}
