package com.apimybarber.domain.entity.mappers;

import com.apimybarber.domain.entity.Pessoa;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.services.interfaces.IUserService;
import com.apimybarber.domain.viewobject.PessoaVO;
import org.springframework.stereotype.Component;

@Component
public class PessoaMapper implements EntityMapper<PessoaVO, Pessoa> {

    private final IUserService userService;

    public PessoaMapper(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public Pessoa toEntity(PessoaVO vo, Pessoa entity) {
        User user = userService.buscar(vo.userId());
        if (user == null) return null;

        if (entity == null) {
            entity = new Pessoa(vo.id(), vo.nome(), vo.numero(), user);
        } else {
            entity.setNome(vo.nome());
            entity.setNumero(vo.numero());
        }
        return entity;
    }
}
