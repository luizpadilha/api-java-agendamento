package com.apimybarber.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Table(name = "servico")
@Entity(name = "servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String descricao;
    private double preco;
    @JsonIgnore
    @ManyToOne
    private User user;
    @JsonIgnore
    private LocalTime tempo;
    @Transient
    private int tempoHora;
    @Transient
    private int tempoMinuto;

    public Servico(String id, String descricao, double preco, User user, LocalTime tempo) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
        this.user = user;
        this.tempo = tempo;
    }
}
