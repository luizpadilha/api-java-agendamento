package com.apimybarber.domain.controllers;

import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.entity.UserRole;
import com.apimybarber.domain.services.TokenService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.viewobject.AuthenticationVO;
import com.apimybarber.domain.viewobject.RegisterVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class AuthenticationControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Spy
    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void login_dadoAuthenticationVOComLoginESenhaInexistente_deveRetornarCampoErroComValorUsuarioInexistenteEStatusOk() throws Exception {
        AuthenticationVO authenticationVO = new AuthenticationVO("test_login", "test_password");
        when(userService.loadUserByUsername(anyString())).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authenticationVO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.erro").value("Usuário inexistente"));

    }


    @Test
    void login_dadoAuthenticationVOComLoginESenhaExistente_deveRetornarCampoTokenComValorTokenEStatusOk() throws Exception {

        String encryptedPassword = new BCryptPasswordEncoder().encode("test_password");
        User newUser = new User("test_login", encryptedPassword, UserRole.ADMIN);
        when(userService.loadUserByUsername(anyString())).thenReturn(newUser);

        AuthenticationVO authenticationVO = new AuthenticationVO(newUser.getLogin(), "test_password");
        tokenService.setSecret("secret123");
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(authenticationVO.login(), authenticationVO.password());
        UsernamePasswordAuthenticationToken userPassword = new UsernamePasswordAuthenticationToken(newUser, authenticationVO.password());
        when(authenticationManager.authenticate(usernamePassword)).thenReturn(userPassword);
        when(tokenService.generateToken(newUser)).thenReturn("token_jwt");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authenticationVO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("token_jwt"));

    }

    @Test
    void register_dadoRegisterVOUsuarioInexistente_deveRetornarStatusOkEChamarServiceParaGravarEStatusOk() throws Exception {
        RegisterVO registerVO = new RegisterVO("test_login", "test_password", UserRole.ADMIN);
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerVO.password());
        User newUser = new User(registerVO.login(), encryptedPassword, registerVO.role());

        when(userService.loadUserByUsername(anyString())).thenReturn(null);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerVO)))
                .andExpect(status().isOk());

        verify(userService, times(1)).gravar(newUser);
    }

    @Test
    void register_dadoRegisterVOUsuarioExistente_deveRetornarStatusBad() throws Exception {
        RegisterVO registerVO = new RegisterVO("test_login", "test_password", UserRole.ADMIN);
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerVO.password());
        User newUser = new User(registerVO.login(), encryptedPassword, registerVO.role());

        when(userService.loadUserByUsername(anyString())).thenReturn(newUser);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerVO)))
                .andExpect(status().isBadRequest());

    }

}
