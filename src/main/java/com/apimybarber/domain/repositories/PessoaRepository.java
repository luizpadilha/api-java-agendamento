package com.apimybarber.domain.repositories;

import com.apimybarber.domain.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PessoaRepository extends JpaRepository<Pessoa, String> {

    List<Pessoa> findAllByUser_Id(String id_user);
}
