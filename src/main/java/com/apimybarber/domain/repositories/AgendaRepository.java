package com.apimybarber.domain.repositories;

import com.apimybarber.domain.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, String> {

    List<Agenda> findAllByUser_Id(String id_user);


}
