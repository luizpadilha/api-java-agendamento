package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.Pessoa;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.services.PessoaService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.viewobject.PessoaVO;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PessoaControllerTest {

    private MockMvc mockMvc;
    @Mock
    private PessoaService pessoaService;
    @Mock
    private UserService userService;
    @Spy
    @InjectMocks
    private PessoaController pessoaController;

    private String id = "123";
    private String idUser = "123";
    private User user = new User(idUser, "login123", "senha123", UserRole.ADMIN);
    private String nome = "nome123";
    private Pessoa pessoa = new Pessoa(id, nome, "44 99999-9999", user);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pessoaController).build();
    }

    @Test
    void pessoas_dadoParamIdUserEArrayPessoa_deveRetornarArrayPessoaComNomeIgualEStatusOk() throws Exception {
        List<Pessoa> expected = Arrays.asList(pessoa);
        when(pessoaService.findAllByUser_Id(anyString())).thenReturn(expected);
        when(userService.loadUserByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/pessoa/pessoas")
                        .param("userId", idUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value(nome));

        verify(pessoaService, times(1)).findAllByUser_Id(idUser);
        verifyNoMoreInteractions(pessoaService);

    }

    @Test
    void salvarPessoa_dadoPessoaVOExistenteEUser_deveAtualizarPessoaESalvarERetornarStatusOk() throws Exception {
        when(userService.buscar(idUser)).thenReturn(user);
        when(pessoaService.buscar(id)).thenReturn(pessoa);
        PessoaVO pessoaVO = new PessoaVO(id, pessoa.getNome(), pessoa.getNumero(), idUser);

        mockMvc.perform(post("/api/pessoa/salvar-pessoa")
                        .content(new ObjectMapper().writeValueAsString(pessoaVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        pessoa.setNome(pessoaVO.nome());
        pessoa.setNumero(pessoaVO.numero());
        verify(pessoaService, times(1)).buscar(id);
        verify(pessoaService, times(1)).gravar(pessoa);
        verifyNoMoreInteractions(pessoaService);

    }

    @Test
    void salvarPessoa_dadoPessoaVOInexistenteEUser_deveCriarPessoaESalvarERetornarStatusOk() throws Exception {
        when(userService.buscar(idUser)).thenReturn(user);
        when(pessoaService.buscar(null)).thenReturn(null);
        PessoaVO pessoaVO = new PessoaVO(null, pessoa.getNome(), pessoa.getNumero(), idUser);

        mockMvc.perform(post("/api/pessoa/salvar-pessoa")
                        .content(new ObjectMapper().writeValueAsString(pessoaVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Pessoa newPessoa = new Pessoa(pessoaVO.id(), pessoaVO.nome(), pessoaVO.numero(), user);
        verify(pessoaService, times(1)).buscar(null);
        verify(pessoaService, times(1)).gravar(newPessoa);
        verifyNoMoreInteractions(pessoaService);

    }

    @Test
    void salvarPessoa_dadoPessoaVOEUserNulo_deveRetornarStatusBad() throws Exception {
        when(userService.buscar(anyString())).thenReturn(null);
        PessoaVO pessoaVO = new PessoaVO(null, pessoa.getNome(), pessoa.getNumero(), idUser);

        mockMvc.perform(post("/api/pessoa/salvar-pessoa")
                        .content(new ObjectMapper().writeValueAsString(pessoaVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void removerPessoa_dadoParamPessoaId_deveRemoverERetornarStatusOk() throws Exception {
        mockMvc.perform(post("/api/pessoa/remover-pessoa")
                        .param("pessoaId", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(pessoaService, times(1)).excluir(id);
        verifyNoMoreInteractions(pessoaService);

    }

}