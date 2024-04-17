package com.apimybarber.domain.repositories;

import com.apimybarber.domain.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, String> {

    @Query(value = "select ad.* from agenda ad " +
            " where ad.user_id = :user_id " +
            " and to_char(ad.horario, 'dd/MM/yyyy') = :horarioFormatado", nativeQuery = true)
    List<Agenda> findAllByUserIdAndHorario(String user_id, String horarioFormatado);


}
