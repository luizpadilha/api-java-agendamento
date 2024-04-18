package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Spy
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private String id = "123";
    private String login = "login123";
    private User user = new User(id, login, "senha123", UserRole.ADMIN);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadUserByUsername_dadoLogin_deveRetornarUser() {
        User expected = user;
        when(userRepository.findByLogin(login)).thenReturn(expected);
        User actual = (User) userService.loadUserByUsername(login);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByLogin(login);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void gravar_dadoUmUserValido_deveChamarSaveDoRepository() {
        assertDoesNotThrow(() -> userService.gravar(user));
        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void buscar_dadoUmIdExistente_deveRetornarUser() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User resultado = userService.buscar(id);

        assertEquals(user, resultado);

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void buscar_dadoUmIdNaoExistente_deveRetornarNull() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        User resultado = userService.buscar(id);

        assertNull(resultado);

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void excluir_dadoUmIdExistente_deveExcluirUser() {
        assertDoesNotThrow(() -> userService.excluir(id));
        verify(userRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(userRepository);
    }
}