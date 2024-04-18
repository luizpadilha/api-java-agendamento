package com.apimybarber.domain.repositories;

import com.apimybarber.domain.entity.Notificacao;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
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
class NotificacaoRepositoryTest {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get Notificacaos successfully from DB")
    void findByLogin_dadoNotificacaoSalvoComUser_deveRetornarNotificacaos() {
        String login = "usuarioAdm";
        User newUser = new User(login, "senha123", UserRole.ADMIN);
        this.salvarUser(newUser);

        Notificacao notificacao = new Notificacao(null, "titulo 1", "descricao 1", newUser);
        this.salvarNotificacao(notificacao);

        List<Notificacao> result = this.notificacaoRepository.findAllByUser_Id(newUser.getId());

        assertTrue(result != null && !result.isEmpty());
    }

    @Test
    @DisplayName("Should not get Notificacaos from DB when Notificacaos not exists")
    void findByLogin_dadoNotificacaoNaoSalvo_deveRetornarVazio() {
        String login = "usuarioAdm";
        User newUser = new User(login, "senha123", UserRole.ADMIN);
        this.salvarUser(newUser);

        List<Notificacao> result = this.notificacaoRepository.findAllByUser_Id(newUser.getId());

        assertTrue(result == null || result.isEmpty());
    }

    private User salvarUser(User user) {
        this.entityManager.persist(user);
        return user;
    }

    private Notificacao salvarNotificacao(Notificacao notificacao) {
        this.entityManager.persist(notificacao);
        return notificacao;
    }


}