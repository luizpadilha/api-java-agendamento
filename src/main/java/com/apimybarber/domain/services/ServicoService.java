package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.services.interfaces.IServicoService;
import com.apimybarber.domain.repositories.ServicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService extends AbstractService<Servico> implements IServicoService {

    private final ServicoRepository repository;

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Servico> findAllByUser_Id(String username) {
        return repository.findAllByUser_Id(username);
    }

    @Override
    public Servico gravar(Servico registro) {
        return repository.save(registro);
    }

    @Override
    public Servico buscar(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void excluir(String id) {
        repository.deleteById(id);
    }
}
