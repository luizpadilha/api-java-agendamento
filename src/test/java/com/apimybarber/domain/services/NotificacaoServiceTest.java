package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.Notificacao;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.repositories.NotificacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacaoServiceTest {


    @Spy
    @InjectMocks
    private NotificacaoService notificacaoService;

    @Mock
    private NotificacaoRepository notificacaoRepository;

    private String id = "123";
    private String idUser = "123";
    private String titulo = "titulo1";
    private User user = new User(idUser, "login123", "senha123", UserRole.ADMIN);
    private Notificacao notificacao = new Notificacao(id, titulo, "descricao1", user);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllByUser_Id_dadoIdUser_deveRetornarNotificacaosPorUser() {
        List<Notificacao> expected = Arrays.asList(notificacao);
        when(notificacaoRepository.findAllByUser_Id(idUser)).thenReturn(expected);
        List<Notificacao> actual = notificacaoService.findAllByUser_Id(idUser);
        assertEquals(expected, actual);
        verify(notificacaoRepository, times(1)).findAllByUser_Id(idUser);
        verifyNoMoreInteractions(notificacaoRepository);
    }

    @Test
    public void gravar_dadoUmNotificaoValido_deveChamarSaveDoRepository() {
        assertDoesNotThrow(() -> notificacaoService.gravar(notificacao));
        verify(notificacaoRepository, times(1)).save(notificacao);
        verifyNoMoreInteractions(notificacaoRepository);
    }

    @Test
    public void buscar_dadoUmIdExistente_deveRetornarNotificacao() {
        when(notificacaoRepository.findById(id)).thenReturn(Optional.of(notificacao));

        Notificacao resultado = notificacaoService.buscar(id);

        assertEquals(notificacao, resultado);

        verify(notificacaoRepository, times(1)).findById(id);
        verifyNoMoreInteractions(notificacaoRepository);
    }

    @Test
    public void buscar_dadoUmIdNaoExistente_deveRetornarNull() {
        when(notificacaoRepository.findById(id)).thenReturn(Optional.empty());

        Notificacao resultado = notificacaoService.buscar(id);

        assertNull(resultado);

        verify(notificacaoRepository, times(1)).findById(id);
        verifyNoMoreInteractions(notificacaoRepository);
    }

    @Test
    public void excluir_dadoUmIdExistente_deveExcluirPessoa() {
        assertDoesNotThrow(() -> notificacaoService.excluir(id));
        verify(notificacaoRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(notificacaoRepository);
    }

}