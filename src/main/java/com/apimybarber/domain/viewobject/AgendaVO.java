package com.apimybarber.domain.viewobject;

public record AgendaVO(String id, String horarioToIso8601, ServicoVO servico, PessoaVO pessoa, String userId) {

}
