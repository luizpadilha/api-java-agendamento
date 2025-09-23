package com.apimybarber.domain.services.interfaces;

import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.enums.TipoPeriodo;
import com.apimybarber.domain.viewobject.AgendaAgrupadaVO;

import java.time.LocalDate;
import java.util.List;

public interface IAgendaService {
    List<Agenda> findAllByUserIdAndHorario(String userId, LocalDate horario);

    List<Agenda> findAllByUserIdAndPessoa(String userId, String pessoaId);

    List<AgendaAgrupadaVO> findAllAgrupadaByUserIdAndPeriodo(String userId, LocalDate inicio, LocalDate fim, TipoPeriodo tipoPeriodo);

    Agenda gravar(Agenda agenda);

    Agenda buscar(String id);

    void excluir(String id);
}
