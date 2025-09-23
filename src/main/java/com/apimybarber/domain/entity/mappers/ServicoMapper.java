package com.apimybarber.domain.entity.mappers;


import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.services.interfaces.IUserService;
import com.apimybarber.domain.viewobject.ServicoVO;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Base64;

@Component
public class ServicoMapper implements EntityMapper<ServicoVO, Servico> {

    private final IUserService userService;

    public ServicoMapper(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public Servico toEntity(ServicoVO vo, Servico entity) {
        User user = userService.buscar(vo.userId());
        byte[] imageBytes = vo.imageBase64() == null ? null : Base64.getDecoder().decode(vo.imageBase64());
        if (user == null) return null;

        LocalTime tempo = LocalTime.parse(vo.tempo());
        if (entity == null) {
            entity = new Servico(vo.id(), vo.descricao(), vo.preco(), user, tempo, imageBytes);
        } else {
            entity.setDescricao(vo.descricao());
            entity.setPreco(vo.preco());
            entity.setTempo(tempo);
            entity.setFileImage(imageBytes);
        }

        return entity;
    }
}
