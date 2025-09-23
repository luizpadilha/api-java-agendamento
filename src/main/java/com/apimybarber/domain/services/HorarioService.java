package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.entity.Configuracao;
import com.apimybarber.domain.entity.ConfiguracaoExpediente;
import com.apimybarber.domain.enums.DiaSemana;
import com.apimybarber.domain.services.interfaces.IConfiguracaoExpedienteService;
import com.apimybarber.domain.services.interfaces.IConfiguracaoService;
import com.apimybarber.domain.services.interfaces.IHorarioService;
import com.apimybarber.domain.viewobject.HorarioVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class HorarioService implements IHorarioService {

    private final IConfiguracaoService configuracaoService;
    private final IConfiguracaoExpedienteService configuracaoExpedienteService;

    public HorarioService(IConfiguracaoService configuracaoService, IConfiguracaoExpedienteService configuracaoExpedienteService) {
        this.configuracaoService = configuracaoService;
        this.configuracaoExpedienteService = configuracaoExpedienteService;
    }

    @Override
    public List<HorarioVO> montarHorariosDisponiveis(LocalTime tempoServico, List<Agenda> horariosAgendados, String userId, LocalDate localDate) {
        List<LocalTime> horariosDisponiveis = new ArrayList<>();
        List<HorarioVO> horarios = new ArrayList<>();

        Configuracao configuracao = configuracaoService.findAllByUser_Id(userId).stream().findFirst().orElse(null);
        if (configuracao == null) {
            configuracao = configuracaoService.criarConfiguracaoPadrao(userId);
        }
        DiaSemana diaSemana = DiaSemana.converterDayOfWeek(localDate.getDayOfWeek());
        ConfiguracaoExpediente configuracaoExpediente = configuracaoExpedienteService.buscarConfiguracaoExpedientePorConfiguracaoEDiaSemana(configuracao.getId(), diaSemana);

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

    @Override
    public void adicionarHorariosDisponiveisPorPeriodo(LocalTime tempoServico, List<Agenda> horariosAgendados, List<LocalTime> horariosDisponiveis, LocalTime horarioAtual, LocalTime horarioFinal) {
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
