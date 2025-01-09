package com.labmanager.labmanager.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Date;
import java.time.LocalDate;

@Data // Lombok gera automaticamente getters, setters, toString, equals e hashCode
@NoArgsConstructor // Lombok gera o construtor sem argumentos
@AllArgsConstructor // Lombok gera o construtor com todos os argumentos
@Entity
@Table(name = "equipamentos")
public class Equipamento {

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

    // Construtor para garantir que a data de cadastro seja preenchida ao criar um novo Equipamento
    public Equipamento(String nome, String descricao, String numeroSerie, String status, String observacoes) {
        this.nome = nome;
        this.descricao = descricao;
        this.numeroSerie = numeroSerie;
        this.status = status;
        this.dataCadastro = Date.valueOf(LocalDate.now()); // Preenche a data de cadastro automaticamente
        this.observacoes = observacoes;
    }

    // Adicionar na classe Equipamento:
    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = Date.valueOf(LocalDate.now());
        }
    }

    // Adicionar na classe Equipamento para garantir a data ao editar:
    @PreUpdate
    protected void onUpdate() {
        if (dataCadastro == null) {
            dataCadastro = Date.valueOf(LocalDate.now());
        }
    }
}
