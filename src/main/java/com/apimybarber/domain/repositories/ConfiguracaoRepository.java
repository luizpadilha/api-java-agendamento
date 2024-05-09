package com.apimybarber.domain.repositories;

import com.apimybarber.domain.entity.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, String> {

    List<Configuracao> findAllByUser_Id(String id_user);
}
