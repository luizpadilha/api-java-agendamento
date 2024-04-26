package com.apimybarber.domain.repositories;

import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.viewobject.AgendaAgrupadaVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, String> {

    @Query(value = "select ad.* from agenda ad " +
            " where ad.user_id = :user_id " +
            " and to_char(ad.horario, 'dd/MM/yyyy') = :horarioFormatado", nativeQuery = true)
    List<Agenda> findAllByUserIdAndHorario(String user_id, String horarioFormatado);

    @Query(value = "select date_trunc('day', ad.horario) as data, sum(serv.preco) as total " +
            " from agenda ad " +
            " inner join servico serv on ad.servico_id = serv.id " +
            " where ad.user_id = :user_id " +
            " and date_trunc('day', ad.horario) between to_date(:localDateInicial, 'dd/MM/yyyy') and to_date(:localDateFinal, 'dd/MM/yyyy') " +
            " group by data " +
            " order by data", nativeQuery = true)
    List<Object[]> findAllAgrupadaByUserIdAndPeriodo(String user_id, String localDateInicial, String localDateFinal);


}
