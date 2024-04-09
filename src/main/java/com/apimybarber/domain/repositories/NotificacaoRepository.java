package com.apimybarber.domain.repositories;

import com.apimybarber.domain.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, String> {

    List<Notificacao> findAllByUser_Id(String id_user);
}
