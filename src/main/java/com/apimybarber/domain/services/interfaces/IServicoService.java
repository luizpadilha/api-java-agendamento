package com.apimybarber.domain.services.interfaces;

import com.apimybarber.domain.entity.Servico;

import java.util.List;

public interface IServicoService {

    List<Servico> findAllByUser_Id(String username);

    Servico gravar(Servico registro);

    Servico buscar(String id);

    void excluir(String id);
}
