package com.labmanager.labmanager.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Lombok gera automaticamente getters, setters, toString, equals e hashCode
@NoArgsConstructor // Lombok gera o construtor sem argumentos
@AllArgsConstructor // Lombok gera o construtor com todos os argumentos
@Entity
@Table(name = "disciplinas")
public class Disciplina {

    // FAZER VALIDAÇÕES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String codigo;

    // Relacionamento com a entidade Usuario (professor responsável pela disciplina)
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false) // Chave estrangeira para a tabela Usuario
    private Usuario professor;
}
