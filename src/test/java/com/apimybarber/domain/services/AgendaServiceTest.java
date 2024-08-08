package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.*;
import com.apimybarber.domain.repositories.AgendaRepository;
import com.apimybarber.domain.utils.LocalDateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendaServiceTest {

    @Spy
    @InjectMocks
    private AgendaService agendaService;

    @Mock
    private AgendaRepository agendaRepository;

    private String id = "123";
    private String idUser = "123";
    private String idServico = "123";
    private String idPessoa = "123";
    private LocalDateTime horario = LocalDateTime.now();
    private User user = new User(idUser, "login123", "senha123", UserRole.ADMIN);
    private Servico servico = new Servico(id, idServico, 15.0, user, null, null);
    private Pessoa pessoa = new Pessoa(id, idPessoa, "44 99999-9999", user);
    private Agenda agenda = new Agenda(id, pessoa, servico, user, horario);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllByUserIdAndHorario_dadoIdUserAndHorario_deveRetornarAgendasPorUser() {
        List<Agenda> expected = Arrays.asList(agenda);
        LocalDate horarioLocalDate = horario.toLocalDate();
        String horarioFormatado = LocalDateUtils.getDataFormatada(horarioLocalDate);
        when(agendaRepository.findAllByUserIdAndHorario(idUser, horarioFormatado)).thenReturn(expected);
        List<Agenda> actual = agendaService.findAllByUserIdAndHorario(idUser, horarioLocalDate);
        assertEquals(expected, actual);
        verify(agendaRepository, times(1)).findAllByUserIdAndHorario(idUser, horarioFormatado);
        verifyNoMoreInteractions(agendaRepository);
    }

    @Test
    public void gravar_dadoUmAgendaValido_deveChamarSaveDoRepository() {
        assertDoesNotThrow(() -> agendaService.gravar(agenda));
        verify(agendaRepository, times(1)).save(agenda);
        verifyNoMoreInteractions(agendaRepository);
    }

    @Test
    public void buscar_dadoUmIdExistente_deveRetornarAgenda() {
        when(agendaRepository.findById(id)).thenReturn(Optional.of(agenda));

        Agenda resultado = agendaService.buscar(id);

        assertEquals(agenda, resultado);

        verify(agendaRepository, times(1)).findById(id);
        verifyNoMoreInteractions(agendaRepository);
    }

    @Test
    public void buscar_dadoUmIdNaoExistente_deveRetornarNull() {
        when(agendaRepository.findById(id)).thenReturn(Optional.empty());

        Agenda resultado = agendaService.buscar(id);

        assertNull(resultado);

        verify(agendaRepository, times(1)).findById(id);
        verifyNoMoreInteractions(agendaRepository);
    }

    @Test
    public void excluir_dadoUmIdExistente_deveExcluirAgenda() {
        assertDoesNotThrow(() -> agendaService.excluir(id));
        verify(agendaRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(agendaRepository);
    }

}