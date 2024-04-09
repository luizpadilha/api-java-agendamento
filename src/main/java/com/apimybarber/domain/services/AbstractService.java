package com.apimybarber.domain.services;

import org.springframework.stereotype.Component;

@Component
public abstract class AbstractService<T> {

    public abstract T gravar(T registro);

    public abstract T buscar(String id);

    public abstract void excluir(String id);

}
