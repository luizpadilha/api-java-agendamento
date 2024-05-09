package com.apimybarber.domain.entity;

import com.apimybarber.domain.enums.DiaSemana;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Table(name = "configuracaoexpediente")
@Entity(name = "configuracaoexpediente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ConfiguracaoExpediente implements Comparable<ConfiguracaoExpediente>{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "inicioexpediente")
    private LocalTime inicioExpediente;
    @Column(name = "finalexpediente")
    private LocalTime finalExpediente;
    @Column(name = "inicioalmoco")
    private LocalTime inicioAlmoco;
    @Column(name = "finalalmoco")
    private LocalTime finalAlmoco;
    @JsonIgnore
    @ManyToOne
    private Configuracao configuracao;
    @Enumerated(EnumType.STRING)
    @Column(name = "diasemana")
    private DiaSemana diaSemana;
    @Transient
    private String idConfig;

    @Override
    public int compareTo(ConfiguracaoExpediente o) {
        return this.getDiaSemana().getDiaDaSemana() - o.getDiaSemana().getDiaDaSemana();
    }
}
