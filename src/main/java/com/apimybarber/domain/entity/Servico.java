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
    @Column(name = "fileimage")
    private byte[] fileImage;
    @JsonIgnore
    @ManyToOne
    private User user;
    @JsonIgnore
    private LocalTime tempo;
    @Transient
    private int tempoHora;
    @Transient
    private int tempoMinuto;
    @Transient
    private String imageBase64;

    public Servico(String id, String descricao, double preco, User user, LocalTime tempo, byte[] fileImage) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
        this.user = user;
        this.tempo = tempo;
        this.fileImage = fileImage;
    }
}
