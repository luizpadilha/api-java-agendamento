package com.apimybarber.domain.services;


import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.repositories.AgendaRepository;
import com.apimybarber.domain.utils.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendaService extends AbstractService<Agenda> {

    @Autowired
    private AgendaRepository repository;

    public List<Agenda> findAllByUser_Id(String username, LocalDate horario) {
        return repository.findAllByUser_IdAndHorario_Date(username, LocalDateUtils.getDataFormatada(horario));
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

