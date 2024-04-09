package com.apimybarber.domain.repositories;

import com.apimybarber.domain.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, String> {

    List<Servico> findAllByUser_Id(String id_user);
}
