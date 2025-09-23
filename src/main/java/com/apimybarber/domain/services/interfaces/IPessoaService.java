package com.apimybarber.domain.services.interfaces;

import com.apimybarber.domain.entity.Pessoa;

import java.util.List;

public interface IPessoaService {

    List<Pessoa> findAllByUser_Id(String username);

    Pessoa gravar(Pessoa registro);

    Pessoa buscar(String id);

    void excluir(String id);
}
