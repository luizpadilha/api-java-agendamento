package com.apimybarber.domain.services;

import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<User> implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = repository.findByLogin(username);
        return user;
    }

    @Override
    public User gravar(User registro) {
        return repository.save(registro);
    }

    @Override
    public User buscar(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void excluir(String id) {
        repository.deleteById(id);
    }
}
