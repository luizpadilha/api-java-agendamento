package com.apimybarber.domain.services.interfaces;

import com.apimybarber.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {

    UserDetails loadUserByUsername(String username);

    User gravar(User registro);

    User buscar(String id);

    void excluir(String id);
}
