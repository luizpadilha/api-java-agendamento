package com.apimybarber.domain.services;


import com.apimybarber.domain.entity.Pessoa;
import com.apimybarber.domain.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService extends AbstractService<Pessoa> {

    @Autowired
    private PessoaRepository repository;

    public List<Pessoa> findAllByUser_Id(String username) {
        return repository.findAllByUser_Id(username);
    }

    @Override
    public Pessoa gravar(Pessoa registro) {
        return repository.save(registro);
    }

    @Override
    public Pessoa buscar(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void excluir(String id) {
        repository.deleteById(id);
    }
}

