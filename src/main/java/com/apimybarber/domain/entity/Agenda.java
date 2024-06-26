package com.apimybarber.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "agenda")
@Entity(name = "agenda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    private Pessoa pessoa;
    @ManyToOne
    private Servico servico;
    @JsonIgnore
    @ManyToOne
    private User user;
    private LocalDateTime horario;
}
