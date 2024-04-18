package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.repositories.ServicoRepository;
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

class ServicoServiceTest {

    @Spy
    @InjectMocks
    private ServicoService servicoService;

    @Mock
    private ServicoRepository servicoRepository;

    private String id = "123";
    private String idUser = "123";
    private String descricao = "login123";
    private User user = new User(idUser, "login123", "senha123", UserRole.ADMIN);
    private Servico servico = new Servico(id, descricao, 15.0, null);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllByUser_Id_dadoIdUser_deveRetornarServicosPorUser() {
        List<Servico> expected = Arrays.asList(servico);
        when(servicoRepository.findAllByUser_Id(idUser)).thenReturn(expected);
        List<Servico> actual = servicoService.findAllByUser_Id(idUser);
        assertEquals(expected, actual);
        verify(servicoRepository, times(1)).findAllByUser_Id(idUser);
        verifyNoMoreInteractions(servicoRepository);
    }

    @Test
    public void gravar_dadoUmServicoValido_deveChamarSaveDoRepository() {
        assertDoesNotThrow(() -> servicoService.gravar(servico));
        verify(servicoRepository, times(1)).save(servico);
        verifyNoMoreInteractions(servicoRepository);
    }

    @Test
    public void buscar_dadoUmIdExistente_deveRetornarServico() {
        when(servicoRepository.findById(id)).thenReturn(Optional.of(servico));

        Servico resultado = servicoService.buscar(id);

        assertEquals(servico, resultado);

        verify(servicoRepository, times(1)).findById(id);
        verifyNoMoreInteractions(servicoRepository);
    }

    @Test
    public void buscar_dadoUmIdNaoExistente_deveRetornarNull() {
        when(servicoRepository.findById(id)).thenReturn(Optional.empty());

        Servico resultado = servicoService.buscar(id);

        assertNull(resultado);

        verify(servicoRepository, times(1)).findById(id);
        verifyNoMoreInteractions(servicoRepository);
    }

    @Test
    public void excluir_dadoUmIdExistente_deveExcluirServico() {
        assertDoesNotThrow(() -> servicoService.excluir(id));
        verify(servicoRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(servicoRepository);
    }
}