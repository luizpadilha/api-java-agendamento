package com.apimybarber.domain.enums;

import java.time.DayOfWeek;

public enum DiaSemana {

    SEGUNDA("Segunda-Feira", 1),
    TERCA("Terça-Feira", 2),
    QUARTA("Quarta-Feira", 3),
    QUINTA("Quinta-Feira", 4),
    SEXTA("Sexta-Feira", 5),
    SABADO("Sábado", 6),
    DOMINGO("Domingo", 7);


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

    public static DiaSemana converterDayOfWeek(DayOfWeek dayOfWeek) {
        for (DiaSemana diaSemana : DiaSemana.values()) {
            if (diaSemana.getDiaDaSemana() == dayOfWeek.getValue()) {
                return diaSemana;
            }
        }
        throw new IllegalArgumentException("Dia da semana inválido: " + dayOfWeek);
    }
}
