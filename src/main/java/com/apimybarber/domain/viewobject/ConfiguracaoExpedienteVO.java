package com.apimybarber.domain.viewobject;

import com.apimybarber.domain.enums.DiaSemana;

public record ConfiguracaoExpedienteVO(String id, String idConfig, String inicioExpediente, String finalExpediente, String inicioAlmoco,
                                       String finalAlmoco, DiaSemana diaSemana) {
}
