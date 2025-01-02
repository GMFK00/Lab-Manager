package com.labmanager.labmanager.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Date;

@Data // Lombok gera automaticamente getters, setters, toString, equals e hashCode
@NoArgsConstructor // Lombok gera o construtor sem argumentos
@AllArgsConstructor // Lombok gera o construtor com todos os argumentos
@Entity
@Table(name = "equipamentos")
public class Equipamento {

    // FAZER VALIDAÇÕES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = true)
    private String descricao;

    @Column(nullable = false, unique = true)
    private String numeroSerie;

    @Column(nullable = false)
    private String status; // Disponível, em manutenção, inoperante

    @Column(nullable = false)
    private Date dataCadastro;

    @Column(nullable = true)
    private String observacoes;
}
