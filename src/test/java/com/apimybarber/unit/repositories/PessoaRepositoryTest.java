package com.apimybarber.unit.repositories;

import com.apimybarber.domain.entity.Pessoa;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.repositories.PessoaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class PessoaRepositoryTest {

    @Autowired
    PessoaRepository pessoaRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get Pessoas successfully from DB")
    void findByLogin_dadoPessoaSalvoComUser_deveRetornarPessoas() {
        String login = "usuarioAdm";
        User newUser = new User(login, "senha123", UserRole.ADMIN);
        this.salvarUser(newUser);

        Pessoa pessoa = new Pessoa(null, "luiz", "44 99999-9999", newUser);
        this.salvarPessoa(pessoa);

        List<Pessoa> result = this.pessoaRepository.findAllByUser_Id(newUser.getId());

        assertTrue(result != null && !result.isEmpty());
    }

    @Test
    @DisplayName("Should not get Pessoas from DB when Pessoas not exists")
    void findByLogin_dadoPessoaNaoSalvo_deveRetornarVazio() {
        String login = "usuarioAdm";
        User newUser = new User(login, "senha123", UserRole.ADMIN);
        this.salvarUser(newUser);

        List<Pessoa> result = this.pessoaRepository.findAllByUser_Id(newUser.getId());

        assertTrue(result == null || result.isEmpty());
    }

    private User salvarUser(User user) {
        this.entityManager.persist(user);
        return user;
    }

    private Pessoa salvarPessoa(Pessoa pessoa) {
        this.entityManager.persist(pessoa);
        return pessoa;
    }


}