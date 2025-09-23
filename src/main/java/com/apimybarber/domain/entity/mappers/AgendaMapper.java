package com.apimybarber.domain.entity.mappers;

import com.apimybarber.domain.entity.Agenda;
import com.apimybarber.domain.entity.Pessoa;
import com.apimybarber.domain.entity.Servico;
import com.apimybarber.domain.entity.User;
import com.apimybarber.domain.services.PessoaService;
import com.apimybarber.domain.services.ServicoService;
import com.apimybarber.domain.services.UserService;
import com.apimybarber.domain.utils.LocalDateUtils;
import com.apimybarber.domain.viewobject.AgendaVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;

@Component
public class AgendaMapper implements EntityMapper<AgendaVO, Agenda> {

    private final PessoaService pessoaService;
    private final ServicoService servicoService;
    private final UserService userService;

    public AgendaMapper(PessoaService pessoaService, ServicoService servicoService, UserService userService) {
        this.pessoaService = pessoaService;
        this.servicoService = servicoService;
        this.userService = userService;
    }

    @Override
    public Agenda toEntity(AgendaVO vo, Agenda entity) {
        User user = userService.buscar(vo.userId());
        Pessoa pessoa = pessoaService.buscar(vo.pessoa().id());
        Servico servico = servicoService.buscar(vo.servico().id());

        if (pessoa == null) {
            pessoa = new Pessoa(vo.id(), vo.pessoa().nome(), vo.pessoa().numero(), user);
        }
        if (servico == null) {
            LocalTime tempo = LocalTime.parse(vo.servico().tempo());
            byte[] imageBytesServico = vo.servico().imageBase64() == null ? null : Base64.getDecoder().decode(vo.servico().imageBase64());
            servico = new Servico(vo.id(), vo.servico().descricao(), vo.servico().preco(), user, tempo, imageBytesServico);
        }
        LocalDateTime horario = LocalDateUtils.getLocalDateTimeIso(vo.horarioToIso8601());

        if (entity == null) {
            entity = new Agenda(vo.id(), pessoa, servico, user, horario);
        } else {
            entity.setPessoa(pessoa);
            entity.setServico(servico);
            entity.setHorario(horario);
        }
        return entity;
    }
}
