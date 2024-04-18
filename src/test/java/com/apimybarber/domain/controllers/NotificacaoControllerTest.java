package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Notificacao;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.services.NotificacaoService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.viewobject.NotificacaoVO;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotificacaoControllerTest {

    private MockMvc mockMvc;
    @Mock
    private NotificacaoService notificacaoService;
    @Mock
    private UserService userService;
    @Spy
    @InjectMocks
    private NotificacaoController notificacaoController;

    private String id = "123";
    private String idUser = "123";
    private String titulo = "titulo1";
    private User user = new User(idUser, "login123", "senha123", UserRole.ADMIN);
    private Notificacao notificacao = new Notificacao(id, titulo, "descricao1", user);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notificacaoController).build();
    }

    @Test
    void notificacoes_dadoParamIdUserEArrayNotificacao_deveRetornarArrayNotificacaoComTituloIgualEStatusOk() throws Exception {
        List<Notificacao> expected = Arrays.asList(notificacao);
        when(notificacaoService.findAllByUser_Id(anyString())).thenReturn(expected);
        when(userService.loadUserByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/notificacao/notificacoes")
                        .param("userId", idUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titulo").value(titulo));

    }

    @Test
    void salvarNotificacoes_dadoNotificacaoVOEUser_deveSalvarERetornarStatusOk() throws Exception {
        when(userService.buscar(anyString())).thenReturn(user);
        NotificacaoVO notificacaoVO = new NotificacaoVO(notificacao.getTitulo(), notificacao.getDescricao(), idUser);

        mockMvc.perform(post("/api/notificacao/salvar-notificacoes")
                        .content(new ObjectMapper().writeValueAsString(notificacaoVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void salvarNotificacoes_dadoNotificacaoVOEUserNulo_deveRetornarStatusBad() throws Exception {
        when(userService.buscar(anyString())).thenReturn(null);
        NotificacaoVO notificacaoVO = new NotificacaoVO(notificacao.getTitulo(), notificacao.getDescricao(), idUser);

        mockMvc.perform(post("/api/notificacao/salvar-notificacoes")
                        .content(new ObjectMapper().writeValueAsString(notificacaoVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}