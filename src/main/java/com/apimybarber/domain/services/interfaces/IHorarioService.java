package com.apimybarber.domain.services.interfaces;

import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.viewobject.HorarioVO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IHorarioService {

    List<HorarioVO> montarHorariosDisponiveis(LocalTime tempoServico, List<Agenda> horariosAgendados, String userId, LocalDate localDate);

    void adicionarHorariosDisponiveisPorPeriodo(LocalTime tempoServico, List<Agenda> horariosAgendados, List<LocalTime> horariosDisponiveis, LocalTime horarioAtual, LocalTime horarioFinal);

}
