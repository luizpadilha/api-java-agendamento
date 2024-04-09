package com.apimybarber.domain.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    private User user;
}
