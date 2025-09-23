package com.apimybarber.domain.services.interfaces;

import com.apimybarber.domain.entity.Configuracao;

import java.util.List;

public interface IConfiguracaoService {

    Configuracao gravar(Configuracao registro);

    Configuracao buscar(String id);

    void excluir(String id);

    List<Configuracao> findAllByUser_Id(String id_user);

    Configuracao inicializarListas(Configuracao configuracao);

    Configuracao criarConfiguracaoPadrao(String userId);

}
