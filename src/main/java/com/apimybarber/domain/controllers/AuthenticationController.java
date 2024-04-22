package com.apimybarber.domain.controllers;


import com.apimybarber.domain.repositories.UserRepository;
import com.apimybarber.domain.services.TokenService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.viewobject.ResponseErroVO;
import com.apimybarber.domain.viewobject.AuthenticationVO;
import com.apimybarber.domain.viewobject.LoginResponseVO;
import com.apimybarber.domain.viewobject.RegisterVO;
import com.apimybarber.domain.entity.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {

    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService service;
    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody AuthenticationVO data) {
        try {
            if (this.service.loadUserByUsername(data.login()) == null) return ResponseEntity.ok(new ResponseErroVO("Usuário inexistente", 400));

            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseVO(token, data.login(), ((User) auth.getPrincipal()).getId(), 120));

        } catch (AuthenticationException e) {
            logger.error("Erro: ", e);
            return ResponseEntity.ok(new ResponseErroVO(e.getMessage(), 400));
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.ok(new ResponseErroVO(e.getMessage(), 500));
        }
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody RegisterVO data) {
        try {
            if (this.service.loadUserByUsername(data.login()) != null) return ResponseEntity.badRequest().build();

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = new User(data.login(), encryptedPassword, data.role());

            this.service.gravar(newUser);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
