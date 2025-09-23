package com.apimybarber.domain.services.interfaces;

import com.apimybarber.domain.entity.Notificacao;

import java.util.List;

public interface INotificacaoService {

    List<Notificacao> findAllByUser_Id(String username);


    Notificacao gravar(Notificacao registro);


    Notificacao buscar(String id);


    void excluir(String id);
}
