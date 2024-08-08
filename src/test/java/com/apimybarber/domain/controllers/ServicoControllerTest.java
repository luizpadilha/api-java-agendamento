package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.services.ServicoService;
import com.apimybarber.domain.services.UserService;
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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ServicoControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ServicoService servicoService;
    @Mock
    private UserService userService;
    @Spy
    @InjectMocks
    private ServicoController servicoController;

    private String id = "123";
    private String idUser = "123";
    private String descricao = "descricao123";
    private User user = new User(idUser, "login123", "senha123", UserRole.ADMIN);
    private Servico servico = new Servico(id, descricao, 15.0, user, LocalTime.now(), null);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(servicoController).build();
    }

    @Test
    void servicos_dadoParamIdUserEArrayServico_deveRetornarArrayServicoComDescricaoIgualEStatusOk() throws Exception {
        List<Servico> expected = Arrays.asList(servico);
        when(servicoService.findAllByUser_Id(anyString())).thenReturn(expected);
        when(userService.loadUserByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/servico/servicos")
                        .param("userId", idUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].descricao").value(descricao));

        verify(servicoService, times(1)).findAllByUser_Id(idUser);
        verifyNoMoreInteractions(servicoService);

    }

    @Test
    void salvarServico_dadoServicoVOExistenteEUser_deveAtualizarServicoESalvarERetornarStatusOk() throws Exception {
        when(userService.buscar(idUser)).thenReturn(user);
        when(servicoService.buscar(id)).thenReturn(servico);
        when(servicoService.gravar(servico)).thenReturn(servico);
        ServicoVO servicoVO = new ServicoVO(id, servico.getDescricao(), servico.getPreco(), idUser, servico.getTempo().format(DateTimeFormatter.ISO_TIME), null);

        mockMvc.perform(post("/api/servico/salvar-servico")
                        .content(new ObjectMapper().writeValueAsString(servicoVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        servico.setDescricao(servicoVO.descricao());
        servico.setPreco(servicoVO.preco());
        verify(servicoService, times(1)).buscar(id);
        verify(servicoService, times(1)).gravar(servico);
        verifyNoMoreInteractions(servicoService);

    }

    @Test
    void salvarServico_dadoServicoVOInexistenteEUser_deveCriarServicoESalvarERetornarStatusOk() throws Exception {
        when(userService.buscar(idUser)).thenReturn(user);
        when(servicoService.buscar(id)).thenReturn(servico);
        when(servicoService.gravar(servico)).thenReturn(servico);
        ServicoVO servicoVO = new ServicoVO(id, servico.getDescricao(), servico.getPreco(), idUser, servico.getTempo().format(DateTimeFormatter.ISO_TIME), null);

        mockMvc.perform(post("/api/servico/salvar-servico")
                        .content(new ObjectMapper().writeValueAsString(servicoVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        LocalTime tempo = LocalTime.parse(servicoVO.tempo());
        Servico newServico = new Servico(servicoVO.id(), servicoVO.descricao(), servicoVO.preco(), user, tempo, null);
        verify(servicoService, times(1)).buscar(id);
        verify(servicoService, times(1)).gravar(newServico);
        verifyNoMoreInteractions(servicoService);

    }

    @Test
    void salvarServico_dadoServicoVOEUserNulo_deveRetornarStatusBad() throws Exception {
        when(userService.buscar(anyString())).thenReturn(null);
        ServicoVO servicoVO = new ServicoVO(null, servico.getDescricao(), servico.getPreco(), idUser, servico.getTempo().format(DateTimeFormatter.ISO_TIME), null);

        mockMvc.perform(post("/api/servico/salvar-servico")
                        .content(new ObjectMapper().writeValueAsString(servicoVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void removerServico_dadoParamServicoId_deveRemoverERetornarStatusOk() throws Exception {
        mockMvc.perform(post("/api/servico/remover-servico")
                        .param("servicoId", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(servicoService, times(1)).excluir(id);
        verifyNoMoreInteractions(servicoService);

    }

}