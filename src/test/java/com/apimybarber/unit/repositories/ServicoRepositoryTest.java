package com.apimybarber.unit.repositories;

import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.repositories.ServicoRepository;
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
class ServicoRepositoryTest {

    @Autowired
    ServicoRepository servicoRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get Servicos successfully from DB")
    void findByLogin_dadoServicoSalvoComUser_deveRetornarServicos() {
        String login = "usuarioAdm";
        User newUser = new User(login, "senha123", UserRole.ADMIN);
        this.salvarUser(newUser);

        Servico servico = new Servico(null, "corte", 15.0, newUser);
        this.salvarServico(servico);

        List<Servico> result = this.servicoRepository.findAllByUser_Id(newUser.getId());

        assertTrue(result != null && !result.isEmpty());
    }

    @Test
    @DisplayName("Should not get Servicos from DB when Servicos not exists")
    void findByLogin_dadoServicoNaoSalvo_deveRetornarVazio() {
        String login = "usuarioAdm";
        User newUser = new User(login, "senha123", UserRole.ADMIN);
        this.salvarUser(newUser);

        List<Servico> result = this.servicoRepository.findAllByUser_Id(newUser.getId());

        assertTrue(result == null || result.isEmpty());
    }

    private User salvarUser(User user) {
        this.entityManager.persist(user);
        return user;
    }

    private Servico salvarServico(Servico servico) {
        this.entityManager.persist(servico);
        return servico;
    }

}