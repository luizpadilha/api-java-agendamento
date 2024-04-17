package com.apimybarber.unit.repositories;

import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.repositories.AgendaRepository;
import com.apimybarber.domain.utils.LocalDateUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class AgendaRepositoryTest {

    @Autowired
    AgendaRepository agendaRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get Agendas successfully from DB")
    void findAllByUser_dadoAgendaSalvoComUser_deveRetornarAgendasPorUser() {
        String login = "usuarioAdm";
        User newUser = new User(login, "senha123", UserRole.ADMIN);
        this.salvarUser(newUser);
        LocalDateTime horario = LocalDateTime.now();

        Agenda newAgenda = new Agenda(null, null, null, newUser, horario);
        this.salvarAgenda(newAgenda);

        List<Agenda> result = this.agendaRepository.findAllByUserIdAndHorario(newUser.getId(), LocalDateUtils.getDataFormatada(horario.toLocalDate()));

        assertTrue(result != null && !result.isEmpty());
    }

    @Test
    @DisplayName("Should not get Agendas from DB when Agendas not exists")
    void findAllByUser_dadoAgendaNaoSalvo_deveRetornarAgendasVazia() {
        String login = "usuarioAdm";
        User newUser = new User(login, "senha123", UserRole.ADMIN);
        this.salvarUser(newUser);
        LocalDateTime horario = LocalDateTime.now();

        List<Agenda> result = this.agendaRepository.findAllByUserIdAndHorario(newUser.getId(), LocalDateUtils.getDataFormatada(horario.toLocalDate()));

        assertTrue(result == null || result.isEmpty());
    }

    private User salvarUser(User user) {
        this.entityManager.persist(user);
        return user;
    }

    private Agenda salvarAgenda(Agenda agenda) {
        this.entityManager.persist(agenda);
        return agenda;
    }


}