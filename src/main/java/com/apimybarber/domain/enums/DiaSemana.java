package com.apimybarber.domain.enums;

public enum DiaSemana {

    DOMINGO("Domingo", 1),
    SEGUNDA("Segunda-Feira", 2),
    TERCA("Terça-Feira", 3),
    QUARTA("Quarta-Feira", 4),
    QUINTA("Quinta-Feira", 5),
    SEXTA("Sexta-Feira", 6),
    SABADO("Sábado", 7);


    private final String descricao;
    private final int diaDaSemana;

    DiaSemana(String descricao, int diaDaSemana) {
        this.descricao = descricao;
        this.diaDaSemana = diaDaSemana;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getDiaDaSemana() {
        return diaDaSemana;
    }
}
