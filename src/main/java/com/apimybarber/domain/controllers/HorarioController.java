package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.*;
import com.apimybarber.domain.enums.DiaSemana;
import com.apimybarber.domain.services.AgendaService;
import com.apimybarber.domain.services.ConfiguracaoService;
import com.apimybarber.domain.services.ServicoService;
import com.apimybarber.domain.services.UserService;
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
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private UserService userService;


    @GetMapping(value = "/horarios-por-data")
    public ResponseEntity<List<HorarioVO>> horariosPorData(@RequestParam String userId,
                                                           @RequestParam String servicoId,
                                                           @RequestParam String data) {
        try {
            LocalDate localDate = LocalDateUtils.getLocalDateIso(data);
            List<Agenda> agendas = agendaService.findAllByUserIdAndHorario(userId, localDate);
            Servico servico = servicoService.buscar(servicoId);
            return ResponseEntity.ok(montarHorariosDisponiveis(servico.getTempo(), agendas, userId, localDate));
        } catch (OutOfMemoryError e) {
            logger.error("Erro: ", e);
            return ResponseEntity.ok(new ArrayList<>());
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    private List<HorarioVO> montarHorariosDisponiveis(LocalTime tempoServico, List<Agenda> horariosAgendados, String userId, LocalDate localDate) {
        List<LocalTime> horariosDisponiveis = new ArrayList<>();
        List<HorarioVO> horarios = new ArrayList<>();

        Configuracao configuracao = configuracaoService.findAllByUser_Id(userId).stream().findFirst().orElse(null);
        if (configuracao == null) {
            User user = userService.buscar(userId);
            configuracao = configuracaoService.criarConfiguracaoPadrao(user);
        }
        DiaSemana diaSemana = DiaSemana.converterDayOfWeek(localDate.getDayOfWeek());
        ConfiguracaoExpediente configuracaoExpediente = configuracaoService.buscarConfiguracaoExpedientePorConfiguracaoEDiaSemana(configuracao.getId(), diaSemana);

        LocalTime inicioExpediente = configuracaoExpediente.getInicioExpediente();
        LocalTime inicioAlmoco = configuracaoExpediente.getInicioAlmoco();
        LocalTime finalAlmoco = configuracaoExpediente.getFinalAlmoco();
        LocalTime finalExpediente = configuracaoExpediente.getFinalExpediente();

        // Etapa da manhã antes do almoço
        LocalTime horarioAtual = inicioExpediente;
        LocalTime horarioFinal = inicioAlmoco;
        adicionarHorariosDisponiveisPorPeriodo(tempoServico, horariosAgendados, horariosDisponiveis, horarioAtual, horarioFinal);

        // Etapa da tarde após o almoço
        horarioAtual = finalAlmoco;
        horarioFinal = finalExpediente;
        adicionarHorariosDisponiveisPorPeriodo(tempoServico, horariosAgendados, horariosDisponiveis, horarioAtual, horarioFinal);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (LocalTime horarioDisponivel : horariosDisponiveis) {
            horarios.add(new HorarioVO(horarioDisponivel.format(formatter), true));
        }
        return horarios;
    }

    private void adicionarHorariosDisponiveisPorPeriodo(LocalTime tempoServico, List<Agenda> horariosAgendados, List<LocalTime> horariosDisponiveis, LocalTime horarioAtual, LocalTime horarioFinal) {
        for (Agenda agenda : horariosAgendados) {
            LocalTime horarioAgendado = agenda.getHorario().toLocalTime();
            while (horarioAtual.plusMinutes(tempoServico.getMinute()).plusHours(tempoServico.getHour()).minusMinutes(1).isBefore(horarioAgendado)
                    && horarioAtual.plusMinutes(tempoServico.getMinute()).plusHours(tempoServico.getHour()).minusMinutes(1).isBefore(horarioFinal)) {
                horariosDisponiveis.add(horarioAtual);
                horarioAtual = horarioAtual.plusMinutes(tempoServico.getMinute()).plusHours(tempoServico.getHour());
            }
            //neccesario para pular o horario marcado, exemplo:
            //horarioAtual = 11:00, horarioAgendado = 11:00
            //resultado: horarioAtual = 12:00
            if (horarioAgendado.plusMinutes(agenda.getServico().getTempo().getMinute()).plusHours(agenda.getServico().getTempo().getHour()).isAfter(horarioAtual)) {
                horarioAtual = horarioAgendado.plusMinutes(agenda.getServico().getTempo().getMinute()).plusHours(agenda.getServico().getTempo().getHour());
            }
        }

        while (horarioAtual.isBefore(horarioFinal)) {
            horariosDisponiveis.add(horarioAtual);
            horarioAtual = horarioAtual.plusHours(tempoServico.getHour());
            horarioAtual = horarioAtual.plusMinutes(tempoServico.getMinute());
        }
    }


}
