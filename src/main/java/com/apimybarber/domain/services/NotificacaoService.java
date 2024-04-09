package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.Notificacao;
import com.apimybarber.domain.repositories.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService extends AbstractService<Notificacao> {

    @Autowired
    private NotificacaoRepository repository;

    public List<Notificacao> findAllByUser_Id(String username) {
        return repository.findAllByUser_Id(username);
    }

    @Override
    public Notificacao gravar(Notificacao registro) {
        return repository.save(registro);
    }

    @Override
    public Notificacao buscar(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void excluir(String id) {
        repository.deleteById(id);
    }
}
