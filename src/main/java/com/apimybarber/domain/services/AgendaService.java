package com.apimybarber.domain.services;


import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.enums.TipoPeriodo;
import com.apimybarber.domain.repositories.AgendaRepository;
import com.apimybarber.domain.utils.LocalDateUtils;
import com.apimybarber.domain.viewobject.AgendaAgrupadaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgendaService extends AbstractService<Agenda> {

    @Autowired
    private AgendaRepository repository;

    public List<Agenda> findAllByUserIdAndHorario(String username, LocalDate horario) {
        return repository.findAllByUserIdAndHorario(username, LocalDateUtils.getDataFormatada(horario));
    }

    public List<AgendaAgrupadaVO> findAllAgrupadaByUserIdAndPeriodo(String username, LocalDate LocalDateInicial, LocalDate LocalDateFinal, TipoPeriodo tipoPeriodo) {
        List<AgendaAgrupadaVO> retorno = new ArrayList<>();
        List<Object[]> dados = new ArrayList<>();
        if (TipoPeriodo.SEMANA.equals(tipoPeriodo)) {
            dados = repository.findAllAgrupadaSemanalByUserIdAndPeriodo(username, LocalDateUtils.getDataFormatada(LocalDateInicial), LocalDateUtils.getDataFormatada(LocalDateFinal));
        } else {
            dados = repository.findAllAgrupadaMensalByUserIdAndPeriodo(username, LocalDateUtils.getDataFormatada(LocalDateInicial), LocalDateUtils.getDataFormatada(LocalDateFinal));
        }
        for (Object[] dado : dados) {
            LocalDate localDate = LocalDateUtils.getLocalDateTimestamp((Timestamp) dado[0]);
            BigDecimal valor = (BigDecimal) dado[1];
            retorno.add(new AgendaAgrupadaVO(localDate, valor));
        }
        return retorno;
    }

    @Override
    public Agenda gravar(Agenda registro) {
        return repository.save(registro);
    }

    @Override
    public Agenda buscar(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void excluir(String id) {
        repository.deleteById(id);
    }
}

