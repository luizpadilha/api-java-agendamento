package com.apimybarber.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "pessoa")
@Entity(name = "pessoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nome;
    private String numero;
    @ManyToOne
    private User user;
}
