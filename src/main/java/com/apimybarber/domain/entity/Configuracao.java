package com.apimybarber.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "configuracao")
@Entity(name = "configuracao")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @JsonIgnore
    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "configuracao", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ConfiguracaoExpediente> expedientes;

    public Configuracao() {
        expedientes = new ArrayList<>();
    }

    public Configuracao(User user) {
        expedientes = new ArrayList<>();
        this.user = user;
    }
}
