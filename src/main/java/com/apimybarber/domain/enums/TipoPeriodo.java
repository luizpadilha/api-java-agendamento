package com.apimybarber.domain.enums;

public enum TipoPeriodo {

    SEMANA("semana"),
    SEMESTRE1("semestre 1"),
    SEMESTRE2("semestre 2");

    private String descricao;

    TipoPeriodo(String descricao) {
        this.descricao = descricao;
    }
}
