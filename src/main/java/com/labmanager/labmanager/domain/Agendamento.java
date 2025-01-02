package com.labmanager.labmanager.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Data // Lombok gera automaticamente getters, setters, toString, equals e hashCode
@NoArgsConstructor // Lombok gera o construtor sem argumentos
@AllArgsConstructor // Lombok gera o construtor com todos os argumentos
public class Agendamento {

    // FAZER VALIDAÇÕES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(nullable = false)
    private String tipo; // Atividade, Disciplina, Manutenção

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) // Define a chave estrangeira
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "laboratorio_id", nullable = false) // Define a chave estrangeira
    private Laboratorio laboratorio;

    @Column(nullable = false)
    private String descricao;
}
