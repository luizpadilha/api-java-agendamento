package com.apimybarber.unit.services;

import com.apimybarber.domain.entity.Pessoa;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.repositories.PessoaRepository;
import com.apimybarber.domain.services.PessoaService;
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

class PessoaServiceTest {

    @Spy
    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    private String id = "123";
    private String idUser = "123";
    private String nome = "nome123";
    private User user = new User(idUser, "login123", "senha123", UserRole.ADMIN);
    private Pessoa pessoa = new Pessoa(id, nome, "44 99999-9999", user);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllByUser_Id_dadoIdUser_deveRetornarPessoasPorUser() {
        List<Pessoa> expected = Arrays.asList(pessoa);
        when(pessoaRepository.findAllByUser_Id(idUser)).thenReturn(expected);
        List<Pessoa> actual = pessoaService.findAllByUser_Id(idUser);
        assertEquals(expected, actual);
        verify(pessoaRepository, times(1)).findAllByUser_Id(idUser);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    public void gravar_dadoUmPessoaValido_deveChamarSaveDoRepository() {
        assertDoesNotThrow(() -> pessoaService.gravar(pessoa));
        verify(pessoaRepository, times(1)).save(pessoa);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    public void buscar_dadoUmIdExistente_deveRetornarPessoa() {
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

        Pessoa resultado = pessoaService.buscar(id);

        assertEquals(pessoa, resultado);

        verify(pessoaRepository, times(1)).findById(id);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    public void buscar_dadoUmIdNaoExistente_deveRetornarNull() {
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        Pessoa resultado = pessoaService.buscar(id);

        assertNull(resultado);

        verify(pessoaRepository, times(1)).findById(id);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    public void excluir_dadoUmIdExistente_deveExcluirPessoa() {
        assertDoesNotThrow(() -> pessoaService.excluir(id));
        verify(pessoaRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(pessoaRepository);
    }

}