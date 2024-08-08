package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.*;
import com.apimybarber.domain.services.AgendaService;
import com.apimybarber.domain.services.PessoaService;
import com.apimybarber.domain.services.ServicoService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.utils.LocalDateUtils;
import com.apimybarber.domain.viewobject.AgendaVO;
import com.apimybarber.domain.viewobject.PessoaVO;
import com.apimybarber.domain.viewobject.ServicoVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AgendaControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ServicoService servicoService;
    @Mock
    private UserService userService;
    @Mock
    private PessoaService pessoaService;
    @Mock
    private AgendaService agendaService;
    @Spy
    @InjectMocks
    private AgendaController agendaController;

    private String id = "123";
    private String idServ = "1234";
    private String idPes = "12345";
    private String idUser = "123456";
    private User user = new User(idUser, "login123", "senha123", UserRole.ADMIN);
    private String descricao = "descricao123";
    private Servico servico = new Servico(idServ, descricao, 15.0, user, LocalTime.now(), null);
    private String nome = "nome123";
    private Pessoa pessoa = new Pessoa(idPes, nome, "44 99999-9999", user);
    private LocalDateTime horario = LocalDateTime.of(2024, 4, 18, 12, 30, 45);
    private Agenda agenda = new Agenda(id, pessoa, servico, user, horario);


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(agendaController).build();
    }

    @Test
    void agendas_dadoParamIdUserEArrayAgenda_deveRetornarArrayAgendaComIdIgualEStatusOk() throws Exception {
        List<Agenda> expected = Arrays.asList(agenda);
        LocalDate horarioLocalDate = horario.toLocalDate();
        when(agendaService.findAllByUserIdAndHorario(idUser, horarioLocalDate)).thenReturn(expected);
        when(userService.loadUserByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/agenda/agendas")
                        .param("userId", idUser)
                        .param("data", LocalDateUtils.getLocalDateStringIso(horario))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(id));

        verify(agendaService, times(1)).findAllByUserIdAndHorario(idUser, horarioLocalDate);
        verifyNoMoreInteractions(agendaService);

    }

    @Test
    void salvarAgenda_dadoAgendaVOExistenteEUserEServicoEPessoa_deveAtualizarAgendaESalvarERetornarStatusOk() throws Exception {
        when(userService.buscar(idUser)).thenReturn(user);
        when(servicoService.buscar(idServ)).thenReturn(servico);
        when(pessoaService.buscar(idPes)).thenReturn(pessoa);
        when(agendaService.buscar(id)).thenReturn(agenda);
        ServicoVO servicoVO = new ServicoVO(idServ, servico.getDescricao(), servico.getPreco(), idUser, servico.getTempo().format(DateTimeFormatter.ISO_TIME), null);
        PessoaVO pessoaVO = new PessoaVO(idPes, pessoa.getNome(), pessoa.getNumero(), idUser);
        AgendaVO agendaVO = new AgendaVO(id, LocalDateUtils.getLocalDateStringIso(horario), servicoVO, pessoaVO, idUser);

        mockMvc.perform(post("/api/agenda/salvar-agenda")
                        .content(new ObjectMapper().writeValueAsString(agendaVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        agenda.setPessoa(pessoa);
        agenda.setServico(servico);
        agenda.setHorario(horario);
        verify(servicoService, times(1)).buscar(idServ);
        verify(pessoaService, times(1)).buscar(idPes);
        verify(agendaService, times(1)).buscar(id);
        verify(agendaService, times(1)).gravar(agenda);
        verifyNoMoreInteractions(agendaService);
    }

    @Test
    void salvarAgenda_dadoAgendaVOInexistenteEUserEServicoEPessoa_deveCriarAgendaESalvarERetornarStatusOk() throws Exception {
        when(userService.buscar(idUser)).thenReturn(user);
        when(servicoService.buscar(idServ)).thenReturn(servico);
        when(pessoaService.buscar(idPes)).thenReturn(pessoa);
        when(agendaService.buscar(id)).thenReturn(null);
        ServicoVO servicoVO = new ServicoVO(idServ, servico.getDescricao(), servico.getPreco(), idUser, servico.getTempo().format(DateTimeFormatter.ISO_TIME), null);
        PessoaVO pessoaVO = new PessoaVO(idPes, pessoa.getNome(), pessoa.getNumero(), idUser);
        AgendaVO agendaVO = new AgendaVO(id, LocalDateUtils.getLocalDateStringIso(horario), servicoVO, pessoaVO, idUser);

        mockMvc.perform(post("/api/agenda/salvar-agenda")
                        .content(new ObjectMapper().writeValueAsString(agendaVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Agenda newAgenda = new Agenda(id, pessoa, servico, user, horario);
        verify(servicoService, times(1)).buscar(idServ);
        verify(pessoaService, times(1)).buscar(idPes);
        verify(agendaService, times(1)).buscar(id);
        verify(agendaService, times(1)).gravar(newAgenda);
        verifyNoMoreInteractions(agendaService);
    }

    @Test
    void salvarAgenda_dadoAgendaVOInexistenteEUserEServicoNuloEPessoaNula_deveCriarAgendaECriarPessoaECriarServicoESalvarERetornarStatusOk() throws Exception {
        when(userService.buscar(idUser)).thenReturn(user);
        when(servicoService.buscar(idServ)).thenReturn(null);
        when(pessoaService.buscar(idPes)).thenReturn(null);
        when(agendaService.buscar(id)).thenReturn(null);
        ServicoVO servicoVO = new ServicoVO(idServ, servico.getDescricao(), servico.getPreco(), idUser, servico.getTempo().format(DateTimeFormatter.ISO_TIME), null);
        PessoaVO pessoaVO = new PessoaVO(idPes, pessoa.getNome(), pessoa.getNumero(), idUser);
        AgendaVO agendaVO = new AgendaVO(id, LocalDateUtils.getLocalDateStringIso(horario), servicoVO, pessoaVO, idUser);

        mockMvc.perform(post("/api/agenda/salvar-agenda")
                        .content(new ObjectMapper().writeValueAsString(agendaVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Pessoa newPessoa = new Pessoa(idPes, agendaVO.pessoa().nome(), agendaVO.pessoa().numero(), user);
        LocalTime tempo = LocalTime.parse(servicoVO.tempo());
        Servico newServico = new Servico(idServ, agendaVO.servico().descricao(), agendaVO.servico().preco(), user, tempo, null);
        Agenda newAgenda = new Agenda(id, newPessoa, newServico, user, horario);
        verify(servicoService, times(1)).buscar(idServ);
        verify(pessoaService, times(1)).buscar(idPes);
        verify(agendaService, times(1)).buscar(id);
        verify(agendaService, times(1)).gravar(newAgenda);
        verifyNoMoreInteractions(agendaService);
    }

    @Test
    void removerAgenda_dadoParamAgendaId_deveRemoverERetornarStatusOk() throws Exception {
        mockMvc.perform(post("/api/agenda/remover-agenda")
                        .param("agendaId", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(agendaService, times(1)).excluir(id);
        verifyNoMoreInteractions(agendaService);

    }

}