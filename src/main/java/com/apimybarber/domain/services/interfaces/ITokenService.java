package com.apimybarber.domain.services.interfaces;

import com.apimybarber.domain.entity.User;

import java.time.Instant;

public interface ITokenService {

    String generateToken(User user);

    String validateToken(String token);

    Instant genExpirationDate();

    void setSecret(String secret);
}
