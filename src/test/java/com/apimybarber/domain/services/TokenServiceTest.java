package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    @Spy
    @InjectMocks
    private TokenService tokenService;

    private User user = new User("id123", "login123", "senha123", UserRole.ADMIN);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void generateToken_dadoUserAndSecret_deveRetornarTokenSemErros() {
        tokenService.setSecret("secret26");
        assertDoesNotThrow(() -> {
            String token = tokenService.generateToken(user);
            assertTrue(token != null && !token.isEmpty());
        });
    }

    @Test
    void validateToken_dadoToken_deveValidarERetornarLoginDoUserSemErros() {
        tokenService.setSecret("secret26");
        assertDoesNotThrow(() -> {
            String token = tokenService.generateToken(user);
            String login = tokenService.validateToken(token);
            assertEquals(user.getLogin(), login);
        });
    }
}