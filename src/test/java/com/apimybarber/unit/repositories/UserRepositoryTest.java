package com.apimybarber.unit.repositories;


import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get User successfully from DB")
    void findByLogin_dadoUserSalvo_deveRetornarUser() {
        String login = "usuarioAdm";
        User newUser = new User(login, "senha123", UserRole.ADMIN);
        this.salvarUser(newUser);

        UserDetails result = this.userRepository.findByLogin(login);

        assertTrue(result != null);
    }

    @Test
    @DisplayName("Should not get User from DB when user not exists")
    void findByLogin_dadoUserNaoSalvo_deveNaoRetornarUser() {
        String login = "usuarioAdm";

        UserDetails result = this.userRepository.findByLogin(login);

        assertTrue(result == null);
    }

    private User salvarUser(User user) {
        this.entityManager.persist(user);
        return user;
    }
}